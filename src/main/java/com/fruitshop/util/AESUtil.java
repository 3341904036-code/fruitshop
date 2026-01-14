package com.fruitshop.util;

import org.apache.commons.codec.binary. Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加密解密工具类
 * 与MySQL的AES_ENCRYPT/AES_DECRYPT函数兼容
 *
 * 密钥:  "hello"
 * 算法: AES/ECB/PKCS5Padding
 */
public class AESUtil {

    private static final String ALGORITHM = "AES";
    // 必须与MySQL数据库中使用的密钥一致
    private static final String SECRET_KEY = "hello";

    /**
     * AES加密（与MySQL AES_ENCRYPT兼容）
     * @param plaintext 明文
     * @return 密文（十六进制字符串）
     */
    public static String encrypt(String plaintext) {
        try {
            // 1. 生成密钥
            byte[] keyBytes = generateKey(SECRET_KEY);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, 0, keyBytes.length, ALGORITHM);

            // 2. 初始化Cipher（ECB模式，PKCS5填充）
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 3. 加密
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // 4. 转换为十六进制字符串
            return new String(Hex.encodeHex(encryptedBytes)).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密（与MySQL AES_DECRYPT兼容）
     * @param ciphertext 密文（十六进制字符串）
     * @return 明文
     */
    public static String decrypt(String ciphertext) {
        try {
            // 1. 生成密钥
            byte[] keyBytes = generateKey(SECRET_KEY);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, 0, keyBytes.length, ALGORITHM);

            // 2. 初始化Cipher（ECB模式，PKCS5填充）
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // 3. 将十六进制字符串转换为字节数组
            byte[] encryptedBytes = Hex.decodeHex(ciphertext.toCharArray());

            // 4. 解密
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 5. 转换为字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 生成AES密钥
     * MySQL AES使用16字节（128位）的密钥
     * @param key 原始密钥字符串
     * @return 16字节的密钥
     */
    private static byte[] generateKey(String key) {
        byte[] keyBytes = new byte[16];
        byte[] sourceBytes = key.getBytes(StandardCharsets.UTF_8);

        // 如果源密钥长度小于16，补充NULL字节
        // 如果源密钥长度大于16，截取前16字节
        int length = Math.min(sourceBytes. length, 16);
        System.arraycopy(sourceBytes, 0, keyBytes, 0, length);

        return keyBytes;
    }

    /**
     * 验证密码是否正确
     * @param plainPassword 明文密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean validatePassword(String plainPassword, String encryptedPassword) {
        try {
            String encrypted = encrypt(plainPassword);
            return encrypted.equalsIgnoreCase(encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 主方法 - 测试加密解密
     */
    public static void main(String[] args) {
        // 测试数据
        String plainText = "100001234 56";  // 用户ID + 密码

        // 加密
        String encrypted = AESUtil.encrypt(plainText);
        System.out.println("明文:   " + plainText);
        System.out.println("密文:   " + encrypted);

        // 解密
        String decrypted = AESUtil.decrypt(encrypted);
        System.out.println("解密:   " + decrypted);

        // 验证
        boolean isValid = AESUtil.validatePassword(plainText, encrypted);
        System.out.println("验证结果:   " + isValid);
    }
}