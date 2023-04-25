/**
 * Project Name:payment
 * File Name:SignUtils.java
 * Package Name:cn.swiftpass.utils.payment.sign
 * Date:2014-6-27下午3:22:33
 */

package net.unmz.java.util.security;

import net.unmz.java.util.xml.XmlUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * Project Name: 常用工具类集合
 * 功能描述：签名用的工具箱
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-12 11:14
 * @since JDK 1.8
 */
public class SignUtils {

    /**
     * 拼接排序好的参数名称和参数值 拼接格式为  [参数名a]=[参数值a]&[参数名b]=[参数值b].....
     *
     * @param maps 参数key-value map集合
     * @return {@code String }
     * @author Faritor
     * @date 2023/04/24
     */
    public static String splitParams(Map<String, String> maps) {
        return splitParams(maps, null);
    }

    /**
     * 拼接排序好的参数名称和参数值 拼接格式为  [参数名a]=[参数值a]&[参数名b]=[参数值b].....
     *
     * @param maps                    参数key-value map集合
     * @param excludedSignatureFields 标志
     * @return {@code String }
     * @author Faritor
     * @date 2023/04/24
     */
    public static String splitParams(Map<String, String> maps, String excludedSignatureFields) {
        StringBuilder paramStr = new StringBuilder();
        Map<String, String> sortMap = new TreeMap<>(maps);
        for (Map.Entry<String, String> entry : sortMap.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())
                    || StringUtils.isBlank(entry.getValue())
                    || Objects.equals(excludedSignatureFields, entry.getKey())) {
                continue;
            }
            paramStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        if (paramStr.length() > 0) {
            return paramStr.substring(1);
        }
        return null;
    }

    public static String getSign(Map<String, String> params) {
        Map<String, String> sortMap = new TreeMap<>(params);
        // 以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if (StringUtils.isBlank(v) || k.equalsIgnoreCase("sign")) {// 过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static boolean checkSign(String xmlString) {
        Map<String, String> map = null;

        try {
            map = XmlUtils.toMap(xmlString.getBytes(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (map != null) {
            String signFromAPIResponse = map.get("sign");
            if (StringUtils.isBlank(signFromAPIResponse)) {
                System.out.println("API返回的数据签名数据不存在，有可能被第三方篡改");
                return false;
            }
            System.out.println("回调里面的签名是:" + signFromAPIResponse);
            String signForAPIResponse = SignUtils.getSign(map);//将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                System.out.println("API返回的数据签名验证不通过，有可能被第三方篡改 signForAPIResponse生成的签名为 " + signForAPIResponse);
                return false;//签名验不过，表示这个API返回的数据有可能已经被篡改了
            }
            System.out.println("恭喜，API返回的数据签名验证通过");
            return true;
        }
        System.out.println("解析xml为空，数据异常或被篡改");
        return false;
    }

    /**
     * 验证返回参数
     *
     * @param params
     * @param key
     * @return
     */
    public static boolean checkParam(Map<String, String> params, String key) {
        boolean result = false;
        if (params.containsKey("sign")) {
            String sign = params.get("sign");
            params.remove("sign");
            StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
            SignUtils.buildPayParams(buf, params, false);
            String preStr = buf.toString();
            String signReceive = MD5Utils.sign(preStr, "&key=" + key, "utf-8");
            result = sign.equalsIgnoreCase(signReceive);
        }
        return result;
    }

    /**
     * 过滤参数
     *
     * @param sArray
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<>(sArray.size());
        if (sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 将map转成String
     *
     * @param payParams
     * @return
     */
    public static String payParamsToString(Map<String, String> payParams) {
        return payParamsToString(payParams, false);
    }

    public static String payParamsToString(Map<String, String> payParams, boolean encoding) {
        return payParamsToString(new StringBuilder(), payParams, encoding);
    }

    public static String payParamsToString(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        buildPayParams(sb, payParams, encoding);
        return sb.toString();
    }

    public static void buildPayParams(StringBuilder sb, Map<String, String> payParams, boolean encoding) {
        List<String> keys = new ArrayList<>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(key).append("=");
            if (encoding) {
                sb.append(urlEncode(payParams.get(key)));
            } else {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }


}

