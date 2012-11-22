package de.pfnrw.jvault.vault;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import de.pfnrw.jvault.util.PasswordGenerator;
import de.pfnrw.jvault.util.Tree;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    Cipher cipher;
    KeyFactory keyFactory;
    X509EncodedKeySpec spec;
    KeyPair keyPair;
    byte[] pk;
    KeyPairGenerator kpg;
    PublicKey pubKey;
    byte[] priv;
    byte[] aes;
    PKCS8EncodedKeySpec specPriv;
    PrivateKey privKey;
    String vaultFilename;
    SecretKeySpec aesKeySpec;
    Cipher aesCipher;
    AlgorithmParameters params;
    SecretKeyFactory factory;
    PBEKeySpec aesSpec;

    public Vault() throws IOException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, JSONException, BadPaddingException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        this("default.jvdb");
    }

    public Vault(String filename) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IOException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, JSONException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        rootEntry = new Entry();
        rootEntry.setName("JVault");
        entryTree = new Tree<Entry>(rootEntry);
        vaultFilename = filename;
        prepareKey();
        open();
    }

    public String encrypt(String dataToEncrypt, Key key, Cipher cipher) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] data = dataToEncrypt.getBytes("UTF-8");

        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);

        return base64Encode(encrypted);
    }

    public String encrypt(byte[] data, Key key, Cipher cipher) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data);

        return base64Encode(encrypted);
    }

    public String decrypt(String data, Key key, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        setCipherMode(key, cipher);

        byte[] plain = cipher.doFinal(base64Decode(data));

        return new String(plain, "UTF-8");
    }

    public byte[] decrypt(byte[] data, Key key, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        setCipherMode(key, cipher);

        return cipher.doFinal(data);
    }

    private void setCipherMode(Key key, Cipher cipher) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if(cipher.getAlgorithm().contains("AES")) {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
    }

    private void prepareKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException, InvalidKeySpecException, NoSuchProviderException, IllegalBlockSizeException, BadPaddingException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        File privKeyFile = new File("jvault_priv.key");
        File pubKeyFile = new File("jvault_pub.key");
        File aesKeyFile = new File("jvault_aes.key");

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        aesCipher = Cipher.getInstance("AES");
        keyFactory = KeyFactory.getInstance("RSA");

        if(!privKeyFile.exists() && !pubKeyFile.exists() && !aesKeyFile.exists()) {
            System.out.println("Generating keys, this may take some time...");
            kpg = KeyPairGenerator.getInstance("RSA");
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            aesSpec = new PBEKeySpec(new PasswordGenerator().generate(32).toCharArray(), new SecureRandom().generateSeed(32), 65536, 256);
            aesKeySpec = new SecretKeySpec(factory.generateSecret(aesSpec).getEncoded(), "AES");
            kpg.initialize(8192);
            keyPair = kpg.genKeyPair();

            pk = keyPair.getPublic().getEncoded();
            priv = keyPair.getPrivate().getEncoded();
            aes = aesKeySpec.getEncoded();

            FileOutputStream outPriv = new FileOutputStream(privKeyFile);
            FileOutputStream outPub = new FileOutputStream(pubKeyFile);
            FileOutputStream outAes = new FileOutputStream(aesKeyFile);

            outPriv.write(base64Encode(priv).getBytes());
            outPub.write(base64Encode(pk).getBytes());

            outPriv.close();
            outPub.close();

            genKeys();

            outAes.write(encrypt(aes, pubKey, cipher).getBytes());
            outAes.close();
        }
        else {
            priv = base64Decode(readFile(privKeyFile.getPath()));
            pk = base64Decode(readFile(pubKeyFile.getPath()));
            genKeys();
            aes = decrypt(base64Decode(readFile(aesKeyFile.getPath())), privKey, cipher);
            aesKeySpec = new SecretKeySpec(aes, "AES");
        }

        aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);

        System.out.println("Successfully initialized keys.");
    }

    public String base64Encode(byte[] data) {
        Base64 coder = new Base64();

        return coder.encodeAsString(data);
    }

    public byte[] base64Decode(String data) {
        Base64 coder = new Base64();
        
        return coder.decode(data);
    }

    private void genKeys() throws InvalidKeySpecException {
        spec = new X509EncodedKeySpec(pk);
        specPriv = new PKCS8EncodedKeySpec(priv);
        privKey = keyFactory.generatePrivate(specPriv);
        pubKey = keyFactory.generatePublic(spec);
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
        FileOutputStream fileOutputStream = new FileOutputStream(vaultFilename);
        fileOutputStream.write(encrypt(getJSONString(), aesKeySpec, aesCipher).getBytes("UTF-8"));
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private String readFile(String filename) throws IOException {
        FileInputStream stream = new FileInputStream(new File(filename));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /* Instead of using default, pass in a decoder. */
            return Charset.forName("UTF-8").decode(bb).toString();
        }
        finally {
            stream.close();
        }
    }

    public void open() throws IOException, JSONException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if(!new File(vaultFilename).exists()) return;

        String jsonString = decrypt(readFile(vaultFilename), aesKeySpec, aesCipher);

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
