package com.conghuhu.utils;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author conghuhu
 * @create 2022-01-30 11:37
 */
public class AESUtil {
    public static final String algorithm = "AES";
    /**
     * // AES/CBC/NOPaddin
     * // AES 默认模式
     * // 使用CBC模式, 在初始化Cipher对象时, 需要增加参数, 初始化向量IV : IvParameterSpec iv = new
     * // IvParameterSpec(key.getBytes());
     * // NOPadding: 使用NOPadding模式时, 原文长度必须是8byte的整数倍
     */
    public static final String transformation = "AES/CBC/NOPadding";
    public static final String key = "1234567812345678";

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            //防止linux下 随机生成key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());

            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(secureRandom);

            SecretKey secretKey = keyGen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, algorithm);
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            // 加密
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * 解密的时候要传入byte数组
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());

            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, algorithm);
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            // 加密
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
