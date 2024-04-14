package com.conghuhu.utils;

import java.util.Base64;

/**
 * @author conghuhu
 * @create 2022-01-30 11:38
 */
public class Base64Util {
    /***
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryBASE64(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }

    /***
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.getEncoder().encodeToString(key);
    }

}
