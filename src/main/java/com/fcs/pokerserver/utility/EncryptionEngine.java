package com.fcs.pokerserver.utility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionEngine {
    private static EncryptionEngine instance = null;
    private SecretKey keySpec = null;
    private AlgorithmParameterSpec ivSpec = null;
    private byte[] key = null;
    private byte[] iv = null;

    public EncryptionEngine() {
    }

    public static EncryptionEngine getInstance() {
        if (instance == null) {
            instance = new EncryptionEngine();
        }
        return instance;
    }

    private void setKey(String secretKey, String ivStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            key = secretKey.getBytes("UTF-8");
            iv = ivStr.getBytes("UTF-8");
//            Hash key and iv base on MD5 algorithm to ensure 16 bytes length.
            key = md.digest(key);
            iv = md.digest(iv);

            keySpec = new SecretKeySpec(key, "AES");
            ivSpec = new IvParameterSpec(iv);
        } catch (Exception e) {
            System.out.println("Error when setKey: " + e.toString());
        }
    }

    public String encrypt(String input, String secret, String ivStr, String algorithm) {
        try {
            setKey(secret, ivStr);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(input.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
            return null;
        }
    }

    public String decrypt(String encryptedStr, String keyStr, String ivStr, String algorithm) {
        try {
            setKey(keyStr, ivStr);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            //Decode encryptedStr from Base64
            byte[] decodedEncryptedStr = Base64.getDecoder().decode(encryptedStr);
            return new String(cipher.doFinal(decodedEncryptedStr), "UTF-8");
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }

}
