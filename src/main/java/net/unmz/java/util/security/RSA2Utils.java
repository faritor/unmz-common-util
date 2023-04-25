package net.unmz.java.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


/**
 * RSA2工具类
 *
 * @author Faritor
 * @date 2023/04/25
 */
public class RSA2Utils {


    // MAX_DECRYPT_BLOCK应等于密钥长度/8（1byte=8bit），所以当密钥位数为2048时，最大解密长度应为256.
    // 128 对应 1024，256对应2048
    private static final int KEY_SIZE = 2048;

    // RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;

    // RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;

    // 不仅可以使用DSA算法，同样也可以使用RSA算法做数字签名
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static final String PUBLIC_KEY = "PublicKey";
    public static final String PRIVATE_KEY = "PrivateKey";

    /**
     * 生成密钥
     *
     * @return {@code Map<String, byte[]> }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static Map<String, byte[]> initKey() throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        // Modulus size must range from 512 to 1024 and be a multiple of 64
        keygen.initialize(KEY_SIZE, secureRandom);
        KeyPair keys = keygen.genKeyPair();
        PrivateKey privateKey = keys.getPrivate();
        PublicKey publicKey = keys.getPublic();
        Map<String, byte[]> map = new HashMap<>(2);
        map.put(PUBLIC_KEY, publicKey.getEncoded());
        map.put(PRIVATE_KEY, privateKey.getEncoded());
        return map;
    }

    /**
     * 用私钥对信息进行数字签名
     *
     * @param privateKey 私钥-base64加密的
     * @param data       加密数据
     * @return {@code String }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static String signByPrivateKey(String privateKey, byte[] data) throws Exception {
        byte[] keyBytes = decryptBASE64(privateKey);
        // 生成私钥
        PrivateKey priKey = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        // 用私钥对信息进行数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());

    }

    /**
     * RSA公钥加密
     *
     * @param publicKey 公钥
     * @param str       加密字符串
     * @return {@code String }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static String encryptByPublicKey(String publicKey, String str) throws Exception {
        // base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        RSAPublicKey pubKey =
                (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(keyBytes));
        // RSA加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        byte[] data = str.getBytes("UTF-8");
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return encryptBASE64(enBytes);
    }

    /**
     * RSA私钥加密
     *
     * @param privateKey 公钥
     * @param str        加密字符串
     * @return {@code String }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static String encryptByPrivateKey(String privateKey, String str) throws Exception {
        // base64编码的公钥
        byte[] keyBytes = decryptBASE64(privateKey);
        RSAPrivateKey priKey =
                (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        // RSA加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        // 加密时超过117字节就报错。为此采用分段加密的办法来加密
        byte[] enBytes = null;
        for (int i = 0; i < data.length; i += MAX_ENCRYPT_BLOCK) {
            // 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_ENCRYPT_BLOCK));
            enBytes = ArrayUtils.addAll(enBytes, doFinal);
        }
        return encryptBASE64(enBytes);
    }

    /**
     * RSA私钥解密
     *
     * @param privateKey 私钥
     * @param encryptStr 加密字符串
     * @return {@code String }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static String decryptByPrivateKey(String privateKey, String encryptStr) throws Exception {
        // base64编码的私钥
        byte[] decoded = decryptBASE64(privateKey);
        RSAPrivateKey priKey =
                (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 64位解码加密后的字符串
        byte[] data = decryptBASE64(encryptStr);
        // 解密时超过128字节报错。为此采用分段解密的办法来解密
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_DECRYPT_BLOCK));
            sb.append(new String(doFinal));
        }
        return sb.toString();
    }

    /**
     * RSA公钥解密
     *
     * @param publicKey     公钥
     * @param encryptStrStr 加密字符串
     * @return {@code String }
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static String decryptByPublicKey(String publicKey, String encryptStrStr) throws Exception {
        // base64编码的私钥
        byte[] decoded = decryptBASE64(publicKey);
        RSAPublicKey priKey =
                (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        // 64位解码加密后的字符串
        byte[] data = decryptBASE64(encryptStrStr);
        // 解密时超过128字节报错。为此采用分段解密的办法来解密
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i += MAX_DECRYPT_BLOCK) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + MAX_DECRYPT_BLOCK));
            sb.append(new String(doFinal));
        }
        return sb.toString();
    }

    /**
     * 验证通过公钥
     *
     * @param publicKey 公钥
     * @param sign      标志
     * @param data      数据
     * @return boolean
     * @throws Exception 异常
     * @author Faritor
     * @date 2023/04/25
     */
    public static boolean verifyByPublicKey(String publicKey, String sign, byte[] data) throws Exception {
        byte[] keyBytes = decryptBASE64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBASE64(sign)); // 验证签名
    }

    /**
     * BASE64Encoder 加密
     *
     * @param data 要加密的数据
     * @return {@code String }
     * @author Faritor
     * @date 2023/04/25
     */
    private static String encryptBASE64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    /**
     * BASE64Encoder 解密
     *
     * @param data 数据
     * @return {@code byte[] }
     * @author Faritor
     * @date 2023/04/25
     */
    private static byte[] decryptBASE64(String data) {
        return Base64.decodeBase64(data);
    }

}