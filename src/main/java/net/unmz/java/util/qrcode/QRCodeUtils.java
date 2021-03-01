package net.unmz.java.util.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Project Name: 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2021-1-26 18:44
 * @since JDK 1.8
 */
public class QRCodeUtils {

    private static final String CHARSET = "utf-8";
    // 二维码尺寸
    private static final int QR_CODE_SIZE = 400;
    // LOGO宽度
    private static final int WIDTH = 100;
    // LOGO高度
    private static final int HEIGHT = 100;

    public static BufferedImage createImageByUrl(String url, String logoUrl) throws Exception {
        return createImage(getQrCodeUrlContent(url), logoUrl);
    }

    public static BufferedImage createImage(String content) throws Exception {
        return createImage(content, null);
    }

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

    public static BufferedImage createImage(String content, String logoUrl) throws Exception {
        BufferedImage image;
        {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            if (logoUrl == null) {
                return image;
            }
        }
        {
            // 如果带logo,则插入logo
            Image src = ImageIO.read(new URL(logoUrl));
            int width = src.getWidth(null);
            int height = src.getHeight(null);

            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image logoImg = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = tag.getGraphics();
            g.drawImage(logoImg, 0, 0, null); // 绘制缩小后的图
            g.dispose();

            // 插入LOGO
            Graphics2D graph = image.createGraphics();
            int x = (QR_CODE_SIZE - width) / 2;
            int y = (QR_CODE_SIZE - height) / 2;
            graph.drawImage(logoImg, x, y, width, height, Color.WHITE, null);
            Shape shape = new RoundRectangle2D.Float(x, y, width, width, 5, 5);
            graph.setStroke(new BasicStroke(3f));
            graph.draw(shape);
            graph.dispose();

        }
        return image;
    }

}
