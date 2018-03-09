/**
 * Project Name:payment
 * File Name:SignUtils.java
 * Package Name:cn.swiftpass.utils.payment.sign
 * Date:2014-6-27下午3:22:33
 */

package net.unmz.java.util.security;

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
     * 验证返回参数
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
     * @param sArray
     * @return
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
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

