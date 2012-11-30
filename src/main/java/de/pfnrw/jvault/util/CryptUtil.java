package de.pfnrw.jvault.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * User: vileda
 * Date: 11/30/12
 * Time: 3:55 PM
 */
public class CryptUtil {
    private Cipher rsaCipher;
    private Cipher aesCipher;
    private KeyPair rsaKeyPair;
    private PrivateKey rsaPrivKey;
    private PublicKey rsaPubKey;
    private SecretKeySpec aesKey;

    public CryptUtil() {
        try {
            rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            aesCipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public void generateRsaKeyPair() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(8192);
            rsaKeyPair = kpg.genKeyPair();
            rsaPrivKey = rsaKeyPair.getPrivate();
            rsaPubKey = rsaKeyPair.getPublic();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void generateAesKey() {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec aesSpec = new PBEKeySpec(new PasswordGenerator().generate(32).toCharArray(), new SecureRandom().generateSeed(32), 65536, 256);
            aesKey = new SecretKeySpec(factory.generateSecret(aesSpec).getEncoded(), "AES");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String encryptAes(String data) throws IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException {
        return encrypt(data, aesKey, aesCipher);
    }

    public String encryptRsa(String data) throws IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException {
        return encrypt(data, rsaPubKey, rsaCipher);
    }

    public byte[] encryptAes(byte[] data) throws IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException {
        return encrypt(data, aesKey, aesCipher);
    }

    public byte[] encryptRsa(byte[] data) throws IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException {
        return encrypt(data, rsaPubKey, rsaCipher);
    }

    private String encrypt(String dataToEncrypt, Key key, Cipher cipher) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return encrypt(dataToEncrypt, key, cipher, "UTF-8");
    }

    private String encrypt(String dataToEncrypt, Key key, Cipher cipher, String encoding) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        return FileUtil.base64Encode(encrypt(dataToEncrypt.getBytes(encoding), key, cipher));
    }

    private byte[] encrypt(byte[] data, Key key, Cipher cipher) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(data);
    }

    public String decryptAes(String data) throws IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(data, aesKey, aesCipher);
    }

    public String decryptRsa(String data) throws IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(data, rsaPrivKey, rsaCipher);
    }

    public String decryptAes(byte[] data) throws IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException {
        return new String(decrypt(data, aesKey, aesCipher));
    }

    public byte[] decryptRsa(byte[] data) throws IllegalBlockSizeException, IOException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException {
        return decrypt(data, rsaPrivKey, rsaCipher);
    }

    private String decrypt(String data, Key key, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        byte[] plain = decrypt(FileUtil.base64Decode(data), key, cipher);

        return new String(plain, "UTF-8");
    }

    private byte[] decrypt(byte[] data, Key key, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        setCipherMode(key, cipher);

        return cipher.doFinal(data);
    }

    private void setCipherMode(Key key, Cipher cipher) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (cipher.getAlgorithm().contains("AES")) {
            cipher.init(Cipher.DECRYPT_MODE, key);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
    }

    public void loadRSAPubKey(File keyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        rsaPubKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(FileUtil.readBase64File(keyFile)));
    }

    public void loadRSAPrivKey(File keyFile) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        rsaPrivKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(FileUtil.readBase64File(keyFile)));
    }

    public void loadAESKey(File keyFile) throws IOException {
        aesKey = new SecretKeySpec(FileUtil.readBase64File(keyFile), "AES");
    }

    public void loadEncryptedKey(File keyFile) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException {
        aesKey = new SecretKeySpec(decryptRsa(FileUtil.readBase64File(keyFile)), "AES");
    }

    public void saveRsaPubKey(File keyFile) {
        FileUtil.saveBase64File(keyFile, rsaPubKey.getEncoded());
    }

    public void saveRsaPrivKey(File keyFile) {
        FileUtil.saveBase64File(keyFile, rsaPrivKey.getEncoded());
    }

    public void saveAesKey(File keyFile) {
        FileUtil.saveBase64File(keyFile, aesKey.getEncoded());
    }

    public void saveEncryptedAesKey(File keyFile) throws IllegalBlockSizeException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, UnsupportedEncodingException {
        FileUtil.saveBase64File(keyFile, encryptRsa(aesKey.getEncoded()));
    }
}
