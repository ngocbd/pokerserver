package com.fsc.pokerserver.test;


import com.fcs.pokerserver.utility.EncryptionEngine;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        String plainText = "dm Danh haha";
//        String keyStr = "day la khoa cua tao";
//        String ivStr = "day la iv cua tao nhe";
//        //MD5 Hash
//        byte[] keyMd5 = md5.digest(keyStr.getBytes());
//        byte[] ivMd5 = md5.digest(ivStr.getBytes());
//
//        System.out.println(keyMd5.length);
//        String keyBase64 = Base64.getEncoder().encodeToString(keyMd5);
//        String ivBase64 = Base64.getEncoder().encodeToString(ivMd5);
//
//        System.out.println("Key: " + keyBase64);
//        System.out.println("iv: " + ivBase64);
//
//
//        SecretKey key = new SecretKeySpec(keyMd5, "AES");
//        AlgorithmParameterSpec iv = new IvParameterSpec(ivMd5);
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
//        System.out.println(Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))));

    }

}
