package net.unmz.java.util.security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Project Name:
 * 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018/3/22 18:25
 * @since JDK 1.8
 */
public class AESUtils {


    /**
     * AES 解密
     * @param key
     * @param encryptedData
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decryptData(String key, String encryptedData, String iv) throws Exception {
        byte[] sessionKeyArray = Base64Utils.getInstance().decoder(key);
        byte[] encryptedDataArray = Base64Utils.getInstance().decoder(encryptedData);
        byte[] ivArray = Base64Utils.getInstance().decoder(iv);

        SecretKeySpec secretKey = new SecretKeySpec(sessionKeyArray, "AES");
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec secKey = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, secKey, new IvParameterSpec(ivArray));// 初始化
        byte[] result = cipher.doFinal(encryptedDataArray);
        return new String(result);
    }


    public static String encryptStr(String content, String password) {
        return new String(encrypt(content, password));
    }
    public static String decryptStr(String cryptograph, String password) {
        return decrypt(cryptograph.getBytes(),password);
    }


    public static byte[] encrypt(String content, String password) {
        try {
            GetAESCipher getAESCipher = new GetAESCipher(password).invoke();
            SecretKeySpec key = getAESCipher.getKey();
            Cipher cipher = getAESCipher.getCipher();

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            byte[] cryptograph = cipher.doFinal(byteContent);
            return Base64Utils.encode(new String(cryptograph)).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String decrypt(byte[] cryptograph, String password) {
        try {
            GetAESCipher getAESCipher = new GetAESCipher(password).invoke();
            SecretKeySpec key = getAESCipher.getKey();
            Cipher cipher = getAESCipher.getCipher();

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] content = cipher.doFinal(Base64Utils.decode(new String(cryptograph)).getBytes());
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] toHash256Deal(String dataStr) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            digester.update(dataStr.getBytes());
            byte[] hex = digester.digest();
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private static class GetAESCipher {
        private String password;
        private SecretKeySpec key;
        private Cipher cipher;

        public GetAESCipher(String password) {
            this.password = password;
        }

        public SecretKeySpec getKey() {
            return key;
        }

        public Cipher getCipher() {
            return cipher;
        }

        public GetAESCipher invoke() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom securerandom = new SecureRandom(toHash256Deal(password));
            keyGenerator.init(256, securerandom);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key = new SecretKeySpec(enCodeFormat, "AES");
            Security.addProvider(new BouncyCastleProvider());
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            return this;
        }
    }
}
