package com.fcs.pokerserver.utility;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionEngine {
    private static EncryptionEngine instance = null;
    private SecretKeySpec keySpec = null;
    private byte[] key = null;

    public EncryptionEngine() {
    }

    public static EncryptionEngine getInstance() {
        if (instance == null) {
            instance = new EncryptionEngine();
        }
        return instance;
    }

    private void setKey(String secretKey) {
        try {
            key = secretKey.getBytes("UTF-8");
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            key = digest.digest(key);
            key = Arrays.copyOf(key, 16);
            keySpec = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            System.out.println("Error when setKey: " + e.toString());
        }
    }

    public String encrypt(String input, String secret, String algorithm) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            return null;
        }
    }

    public String decrypt(String encryptedStr, String keyStr, String algorithm) {
        try {
            setKey(keyStr);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //Decode encryptedStr from Base64
            byte[] decodedEncryptedStr = Base64.getDecoder().decode(encryptedStr);
            return new String(cipher.doFinal(decodedEncryptedStr), "UTF-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }

}
