package net.unmz.java.util.code;

import java.util.Random;

/**
 * Project Name:
 * 功能描述：随机字符串/数字工具类
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2020-5-6 17:33
 * @since JDK 1.8
 */
public class RandomUtils {


    private static final char[] strArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final char[] random = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '-', '=', '+', '~', '[', ']', '|', ':', ';', ',', '.', '/', '<', '>',//移除' ' \ ? `不参与密码排序
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    /**
     * 自定义长度的数字验证码
     *
     * @param length 验证码位数长度
     * @return 返回生成的数字验证码
     */
    public static int getRandomNumber(int length) {//-2147483648~2147483647
        return StrCodeUtils.getNumberCode(length);
    }

    /**
     * 自定义长度的字符串验证码
     *
     * @param length 验证码位数长度
     * @return 返回生成的字符串验证码
     */
    public static String getRandom(int length) {
        return getRandomString(length, strArray);
    }

    public static String getRandomSpecialSymbol(int length) {
        return getRandomString(length, random);
    }

    private static String getRandomString(int length, char[] array) {
        if (length <= 0) {
            throw new IllegalArgumentException("length <= 0");
        }

        StringBuffer str = new StringBuffer();
        while (str.length() <= length - 1)
            str.append(array[new Random().nextInt(array.length)]);
        return str.toString();
    }

}
