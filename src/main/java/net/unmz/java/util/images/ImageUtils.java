package net.unmz.java.util.images;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Project Name:
 * 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2019-10-31 17:50
 * @since JDK 1.8
 */
public class ImageUtils {


    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param imageSrc 源图片地址
     * @param imgDist  目标图片地址
     */
    public static void reduceImg(String imageSrc, String imgDist) {
        try {
            File srcFile = new File(imageSrc);
            File dist = new File(imgDist);
            reduceImage(srcFile, dist);
        } catch (Exception ef) {
            ef.printStackTrace();
        }
    }

    /**
     * 指定图片宽度和高度和压缩比例对图片进行压缩
     *
     * @param srcFile 源图片地址
     * @param imgDist 目标图片地址
     */
    public static void reduceImg(File srcFile, File imgDist) {
        try {
            reduceImage(srcFile, imgDist);
        } catch (Exception ef) {
            ef.printStackTrace();
        }
    }

    private static void reduceImage(File srcFile, File imgDist) throws IOException {
        // 检查图片文件是否存在
        if (!srcFile.exists()) {
            throw new RuntimeException("文件不存在");
        }
        int[] results = getImgWidthHeight(srcFile);

        int widthDist = results[0];
        int heightDist = results[1];
        // 开始读取文件并进行压缩
        Image src = ImageIO.read(srcFile);

        // 构造一个类型为预定义图像类型之一的 BufferedImage
        BufferedImage tag = new BufferedImage(widthDist, heightDist, BufferedImage.TYPE_INT_RGB);

        // 这边是压缩的模式设置
        tag.getGraphics().drawImage(src.getScaledInstance(widthDist, heightDist, Image.SCALE_SMOOTH), 0, 0, null);

        //创建文件输出流
        FileOutputStream out = new FileOutputStream(imgDist);
        //将图片按JPEG压缩，保存到out中
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(tag);
        //关闭文件输出流
        out.close();
    }

    /**
     * 获取图片宽度和高度
     *
     * @param file 图片路径
     * @return 返回图片的宽度
     */
    public static int[] getImgWidthHeight(File file) {
        InputStream is;
        BufferedImage src;
        int result[] = {0, 0};
        try {
            // 获得文件输入流
            is = new FileInputStream(file);
            // 从流里将图片写入缓冲图片区
            src = ImageIO.read(is);
            // 得到源图片宽
            result[0] = src.getWidth(null);
            // 得到源图片高
            result[1] = src.getHeight(null);
            is.close();  //关闭输入流
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        return result;
    }

}
