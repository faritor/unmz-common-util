package net.unmz.java.util.security;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.SignatureException;

/**
 * Project Name: 常用工具类集合
 * 功能描述：MD5签名
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-12 11:14
 * @since JDK 1.8
 */
public class MD5Utils {

    private static final char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 32位MD5加密算法
     * @param s
     * @return
     */
    public static String convert32(String s)
    {
        try {
            byte[] bytes = s.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * 16位MD5加密算法
     * @param s
     * @return
     */
    public static String convert16(String s)
    {
        try {
            byte[] bytes = s.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0x0f];
                chars[k++] = hexChars[b & 0x0f];
            }
            return new String(chars).substring(8, 24);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 获取文件的MD5值 32位
     * @param file 文件
     * @return 32位MD5值
     */
    public static String convertFile32(File file)
    {
        if(file==null || !file.exists())
        {
            return null;
        }
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while ((numRead = fis.read(buffer)) > 0)
            {
                md5.update(buffer, 0, numRead);
            }
            byte[] b=md5.digest();
            StringBuilder sb = new StringBuilder(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                sb.append(hexChars[(b[i] & 0xf0) >>> 4]);
                sb.append(hexChars[b[i] & 0x0f]);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(fis!=null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}