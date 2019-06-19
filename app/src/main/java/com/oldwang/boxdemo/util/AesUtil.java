package com.oldwang.boxdemo.util;

import javax.crypto.SecretKey;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;

/**
 * AES加解密工具类
 */
public  class AesUtil {
    /**
     * 加解密密钥
     */
    private static final String AES_KEY = "e03073ee091a89ce";
    /**
     * 偏移量
     */
    private static final String ivString = "5def0afe4824502e";
    /**
     * 填充方式
     */
    private static final String PADDING = "AES/CBC/PKCS5Padding";
    private static final int LENGTH = 16;
    private static AES aes;

    static {
        SecretKey secretKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), AES_KEY.getBytes());
        aes = SecureUtil.aes();
        aes.setIv(ivString.getBytes());
        aes.init(PADDING, secretKey);
    }

    public static String encode(String str) {
        return aes.encryptBase64(str);
    }

    public static String decode(String str) {
        return new String(aes.decryptStr(str));
    }

}