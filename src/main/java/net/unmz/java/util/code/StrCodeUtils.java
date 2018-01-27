package net.unmz.java.util.code;

import java.util.Random;

/**
 * Project Name: 常用工具类集合
 * 功能描述：数字/字符验证码工具类
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018/1/23 15:30
 * @since JDK 1.8
 */
public class StrCodeUtils {

    private static char[] strArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 自定义长度的数字验证码
     *
     * @param length 验证码位数长度
     * @return 返回生成的数字验证码
     */
    public static int getNumberCode(int length) {//-2147483648~2147483647
        if(length > 9)
            new Exception("The number is too big");
        Random random = new Random();
        StringBuffer max = new StringBuffer().append("8");
        StringBuffer min = new StringBuffer().append("1");
        while (max.length() <= length - 1) {
            max.append("9");
            min.append("0");
        }
        int maxNumber = Integer.parseInt(max.toString());
        int minNumber = Integer.parseInt(min.toString());
        return random.nextInt(maxNumber) + minNumber;
    }

    /**
     * 自定义长度的字符串验证码
     * @param length 验证码位数长度
     * @return 返回生成的字符串验证码
     */
    public static String getStrCode(int length) {
        StringBuffer str = new StringBuffer();
        while (str.length() <= length - 1)
            str.append(strArray[new Random().nextInt(strArray.length)]);
        return str.toString();
    }

}
