package de.pfnrw.jvault.vault;

import de.pfnrw.jvault.util.CryptUtil;
import de.pfnrw.jvault.util.FileUtil;
import de.pfnrw.jvault.util.Tree;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: vileda
 * Date: 11/16/12
 * Time: 12:33 AM
 */
public class Vault implements Serializable {
    Tree<Entry> entryTree;
    Entry rootEntry;
    String vaultFilename;
    CryptUtil cryptUtil;

    public Vault() throws IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, JSONException, BadPaddingException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        this("default.jvdb");
    }

    public Vault(String filename) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, JSONException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        rootEntry = new Entry();
        rootEntry.setName("JVault");
        entryTree = new Tree<Entry>(rootEntry);
        vaultFilename = filename;
        cryptUtil = new CryptUtil();
        prepareKey();
        open();
    }

    private void prepareKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidKeySpecException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        File privKeyFile = new File("jvault_priv.key");
        File pubKeyFile = new File("jvault_pub.key");
        File aesKeyFile = new File("jvault_aes.key");

        if(!privKeyFile.exists() && !pubKeyFile.exists() && !aesKeyFile.exists()) {
            System.out.println("Generating keys, this may take some time...");
            cryptUtil.generateRsaKeyPair();
            cryptUtil.generateAesKey();
            cryptUtil.saveRsaPrivKey(privKeyFile);
            cryptUtil.saveRsaPubKey(pubKeyFile);
            cryptUtil.saveEncryptedAesKey(aesKeyFile);
        }
        else {
            cryptUtil.loadRSAPrivKey(privKeyFile);
            cryptUtil.loadRSAPubKey(pubKeyFile);
            cryptUtil.loadEncryptedKey(aesKeyFile);
        }

        System.out.println("Successfully initialized keys.");
    }

    public String getJSONString() throws JSONException {
        JSONArray jsonArray = new JSONArray();

        treeToJSON(jsonArray, entryTree);

        return jsonArray.toString();
    }

    private void treeToJSON(JSONArray jsonArray, Tree<Entry> root) throws JSONException {
        for(Tree<Entry> e : root.getSubTrees()) {
            JSONObject o = new JSONObject(e.getHead());
            o.put("children", new JSONArray());
            treeToJSON(o.getJSONArray("children"), e);
            jsonArray.put(o);
        }
    }

    public ArrayList<Entry> search(String phrase) {
        return search(phrase, entryTree, null);
    }

    public ArrayList<Entry> search(String phrase, Tree<Entry> root, ArrayList<Entry> results) {
        if(results == null) {
            results = new ArrayList<>();
        }
        for(Tree<Entry> e : root.getSubTrees()) {
            JSONObject o = new JSONObject(e.getHead());
            if(o.toString().contains(phrase) || "*".equals(phrase)) results.add(e.getHead());
            search(phrase, e, results);
        }

        return results;
    }

    public void save() throws IOException, JSONException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException {
        FileUtil.saveFile(vaultFilename, cryptUtil.encryptAes(getJSONString()));
    }

    public void open() throws IOException, JSONException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if(!new File(vaultFilename).exists()) return;

        String jsonString = cryptUtil.decryptAes(FileUtil.readBase64File(vaultFilename));

        JSONArray jsonArray = new JSONArray(jsonString);

        entryTree = new Tree<Entry>(rootEntry);

        mapJSON(jsonArray, rootEntry);

        System.out.println("Container successfully opened.");
    }

    private void mapJSON(JSONArray jsonArray, Entry parent) throws JSONException, IllegalBlockSizeException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException {
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Entry entry;

            if(jsonObject.has("uuid"))
                entry = new Entry(UUID.fromString(jsonObject.getString("uuid")));
            else
                entry = new Entry();

            if(jsonObject.has("name"))
                entry.setName(jsonObject.getString("name"));
            if(jsonObject.has("description"))
                entry.setDescription(jsonObject.getString("description"));
            if(jsonObject.has("url"))
                entry.setUrl(jsonObject.getString("url"));
            if(jsonObject.has("username"))
                entry.setUsername(jsonObject.getString("username"));
            if(jsonObject.has("password"))
                entry.setPassword(jsonObject.getString("password"));

            addChild(parent, entry);

            if(jsonObject.has("children"))
                mapJSON(jsonObject.getJSONArray("children"), entry);
        }
    }

    public void addChild(Entry entry) throws IllegalBlockSizeException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, JSONException {
        entryTree.addLeaf(rootEntry, entry);
        save();
    }

    public void addChild(Entry root, Entry entry) throws IllegalBlockSizeException, IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, JSONException {
        entryTree.addLeaf(root, entry);
        save();
    }
}
