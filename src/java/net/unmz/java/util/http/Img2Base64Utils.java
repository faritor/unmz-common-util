package net.unmz.java.util.http;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * Project Name: 酷配车险
 * 功能描述：
 * 将图片转换为Base64<br>
 * 将base64编码字符串解码成img图片
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-11 16:49
 * @since JDK 1.8
 */

public class Img2Base64Utils {

    public static void main(String[] args) {
        String imgFile = "C:\\Users\\Administrator\\Desktop\\photo\\1.jpg";//待处理的图片
        String imgBase = getImgStr(imgFile);
        System.out.println(imgBase.length());
        System.out.println(imgBase);
        String imgFilePath = "C:\\Users\\Administrator\\Desktop\\photo\\332.jpg";//新生成的图片
        generateImage(imgBase, imgFilePath);
    }

    /**
     * 将图片转换成Base64编码
     *
     * @param imgFile 待处理图片
     * @return
     */
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理

        InputStream in;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return new String(Base64.encodeBase64(data));

    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr      图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public static boolean generateImage(String imgStr, String imgFilePath) {
        if (imgStr == null) //图像数据为空
            return false;

        try {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}