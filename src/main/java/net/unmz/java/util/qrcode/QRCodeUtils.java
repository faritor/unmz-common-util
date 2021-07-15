package net.unmz.java.util.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Project Name: 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2021-1-26 18:44
 * @since JDK 1.8
 */
public class QRCodeUtils {

    /**
     * 字符集
     */
    private static final String CHARSET = "utf-8";
    /**
     * 二维码尺寸
     */
    private static final int QR_CODE_SIZE = 500;
    /**
     * LOGO宽度
     */
    private static final int WIDTH = 100;
    /**
     * LOGO高度
     */
    private static final int HEIGHT = 100;
    /**
     * 创建二维码全局静态初始值
     */
    private static final Hashtable<EncodeHintType, Object> HINTS = new Hashtable<>();

    static {
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        HINTS.put(EncodeHintType.CHARACTER_SET, CHARSET);
        HINTS.put(EncodeHintType.MARGIN, 1);
    }

    /**
     * 按url创建图像
     * 创建二维码
     *
     * @param url     二维码URL地址,获取并解析内容
     * @param logoUrl logoURL地址
     * @return {@link BufferedImage}
     * @throws Exception 例外
     */
    public static BufferedImage createImageByUrl(String url, String logoUrl) throws Exception {
        return createImage(getQrCodeUrlContent(url), logoUrl);
    }

    /**
     * 创建二维码
     *
     * @param content 内容
     * @return {@link BufferedImage}
     * @throws Exception 例外
     */
    public static BufferedImage createImage(String content) throws Exception {
        return createImage(content, "");
    }


    /**
     * 创建二维码
     *
     * @param content 内容
     * @param logoUrl 徽标url
     * @return {@link BufferedImage}
     * @throws Exception 例外
     */
    public static BufferedImage createImage(String content, String logoUrl) throws Exception {
        Image src = StringUtils.isEmpty(logoUrl) ? null : ImageIO.read(new URL(logoUrl));
        return createImage(content, src);
    }

    /**
     * 创建二维码(开源标准版)
     *
     * @param content 内容
     * @param src     logo 对象
     * @return {@link BufferedImage}
     * @throws Exception 例外
     */
    public static BufferedImage createImage(String content, Image src) throws Exception {
        BufferedImage image;
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, HINTS);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        if (src != null) {
            setLogo(src, image);
        }
        return image;
    }


    /**
     * 创建二维码(二维码样式优化版)
     *
     * @param content 内容
     * @return {@link BufferedImage}
     */
    public static BufferedImage createBeautifyQrCode(String content) throws Exception {
        return createBeautifyQrCode(content, "");
    }

    /**
     * 创建美化二维码
     * 创建二维码(二维码样式优化版)
     *
     * @param content 内容
     * @param logoUrl logo url
     * @return {@link BufferedImage}
     * @throws Exception 例外
     */
    public static BufferedImage createBeautifyQrCode(String content, String logoUrl) throws Exception {
        Image src = StringUtils.isEmpty(logoUrl) ? null : ImageIO.read(new URL(logoUrl));
        return createBeautifyQrCode(content, src);
    }

    /**
     * 创建二维码(二维码样式优化版)
     *
     * @param content 内容
     * @param src     logo 对象
     * @return {@link BufferedImage}
     * @throws WriterException 写入程序异常
     */
    public static BufferedImage createBeautifyQrCode(String content, Image src) throws WriterException {
        BufferedImage image = renderQrImage(Encoder.encode(content, ErrorCorrectionLevel.H, HINTS));
        if (src != null) {
            setLogo(src, image);
        }
        return image;
    }

    /**
     * 解析二维码
     *
     * @param url 二维码地址
     * @return
     * @throws Exception
     */
    public static String getQrCodeUrlContent(String url) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new URL(url).openStream());
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);
        HashMap<DecodeHintType, Object> decodeHints = new HashMap<>();
        decodeHints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, decodeHints);
        return result.getText();
    }

    /**
     * 设置 logo
     *
     * @param src   logo 对象
     * @param image 二维码
     */
    public static void setLogo(Image src, BufferedImage image) {
        int width = src.getWidth(null);
        int height = src.getHeight(null);

        if (width > WIDTH) {
            width = WIDTH;
        }
        if (height > HEIGHT) {
            height = HEIGHT;
        }
        Image logoImg = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        Graphics2D graph = image.createGraphics();
        int x = (QR_CODE_SIZE - width) / 2;
        int y = (QR_CODE_SIZE - height) / 2;
        graph.drawImage(logoImg, x, y, width, height, Color.WHITE, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, height, 0, 0);
        graph.draw(shape);
        graph.setStroke(new BasicStroke(10f));
        graph.dispose();
    }

    /**
     * 渲染二维码图片
     *
     * @param code 代码
     * @return {@link BufferedImage}
     */
    public static BufferedImage renderQrImage(QRCode code) {
        int quietZone = 4;
        BufferedImage image = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置背景为白色
        graphics.setBackground(Color.white);
        // 重置画板
        graphics.clearRect(0, 0, QR_CODE_SIZE, QR_CODE_SIZE);
        // 点的颜色
        graphics.setColor(Color.black);

        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }

        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        int outputWidth = Math.max(QR_CODE_SIZE, qrWidth);
        int outputHeight = Math.max(QR_CODE_SIZE, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        final int finderPatternSize = 7;
        final float circleScaleDownFactor = 21f / 30f;
        int circleSize = (int) (multiple * circleScaleDownFactor);

        // 二维数组遍历 进行画点操作
        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    if (!(inputX <= finderPatternSize && inputY <= finderPatternSize ||
                            inputX >= inputWidth - finderPatternSize && inputY <= finderPatternSize ||
                            inputX <= finderPatternSize && inputY >= inputHeight - finderPatternSize)) {
                        graphics.fillRect(outputX, outputY, circleSize, circleSize);
                    }
                }
            }
        }

        int circleDiameter = multiple * finderPatternSize;
        // 左上角定位器
        drawFinderPatternCircleStyle(graphics, leftPadding, topPadding, circleDiameter);
        // 右上角定位器
        drawFinderPatternCircleStyle(graphics, leftPadding + (inputWidth - finderPatternSize) * multiple, topPadding, circleDiameter);
        //左下角定位器
        drawFinderPatternCircleStyle(graphics, leftPadding, topPadding + (inputHeight - finderPatternSize) * multiple, circleDiameter);
        return image;
    }

    /**
     * 绘图定位器图案圆样式
     *
     * @param graphics       绘图
     * @param x              x
     * @param y              Y
     * @param circleDiameter 圆直径
     */
    private static void drawFinderPatternCircleStyle(Graphics2D graphics, int x, int y, int circleDiameter) {
        final int whiteCircleDiameter = circleDiameter * 5 / 7;
        final int whiteCircleOffset = circleDiameter / 7;
        final int middleDotDiameter = circleDiameter * 3 / 7;
        final int middleDotOffset = circleDiameter * 2 / 7;

        // 定位器最外层设置为黑色 矩形
        graphics.setColor(Color.black);
        graphics.fillRect(x, y, circleDiameter, circleDiameter);

        // 定位器中间部分设置为白色 矩形
        graphics.setColor(Color.white);
        graphics.fillRect(x + whiteCircleOffset, y + whiteCircleOffset, whiteCircleDiameter, whiteCircleDiameter);

        // 定位器最里面设置为黑色 矩形
        graphics.setColor(Color.black);
        graphics.fillRect(x + middleDotOffset, y + middleDotOffset, middleDotDiameter, middleDotDiameter);
    }

    public static void main(String[] args) throws Exception {
        String text = "https://www.baidu.com"; // 这里设置自定义网站url
        String logoPath = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2957705046,1654500225&fm=26&gp=0.jpg";
        String destPath = "E:\\demo\\";
        String fileName = destPath + UUID.randomUUID().toString().replace("-", "") + ".png";
        ImageIO.write(createBeautifyQrCode(text, logoPath), "png", new File(fileName));
        System.out.println(fileName);
    }

}

