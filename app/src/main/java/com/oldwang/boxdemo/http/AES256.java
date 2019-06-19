package com.oldwang.boxdemo.http;


import com.oldwang.boxdemo.util.UnlimitedKeyStrengthJurisdictionPolicy;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

    /**
     * 加解密密钥
     */
    private static final String AES_KEY = "e03073ee091a89ce";
    /**
     * 偏移量
     */
    private static final String ivString = "5def0afe4824502e";

    static {
        /*AES 加密默认128位的key，这里改成256位的（类在下面粘出来了）*/
        UnlimitedKeyStrengthJurisdictionPolicy.ensure();
    }

    /*加密算法*/
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     *
     * @param srcData 要加密的数组（String 需要base64 编码）
     * @return 加密后的byte数组
     * @throws Exception 找不到加密算法等
     */
    public static byte[] AES_cbc_encrypt(byte[] srcData) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher.getMaxAllowedKeyLength(ALGORITHM);
        //密钥向量
        byte[] iv = ivString.getBytes();
        //密钥
        byte[] key= AES_KEY.getBytes();
//        byte[] key = keyString.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
        return cipher.doFinal(srcData);
    }

    /**
     *
     * @param encData 要解密的数组
     * @return 解密后的byte数组
     * @throws Exception 找不到解密算法等
     */
    public static byte[] AES_cbc_decrypt(byte[] encData) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher.getMaxAllowedKeyLength(ALGORITHM);
        //密钥向量
        byte[] iv = ivString.getBytes();
        //密钥
        byte[] key= AES_KEY.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
        return cipher.doFinal(encData);
    }
    /**
     *
     * 说明 :将密钥转行成SecretKeySpec格式
     *
     * @param password
     *            16进制密钥
     * @return SecretKeySpec格式密钥
     */
    private static SecretKeySpec getKey(String password)
            throws UnsupportedEncodingException {
        // 如果为128将长度改为128即可
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);
        byte[] passwordBytes = toByte(password);
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }

    /**
     * 将16进制转换为byte数组
     *
     * @param hexString
     *            16进制字符串
     * @return byte数组
     */
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

}
