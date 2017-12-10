package net.unmz.java.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * Project Name: 常用工具类集合
 * 功能描述：json工具类
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-09 20:30
 * @since JDK 1.8
 */
public class JsonUtil {

    private static final SerializeConfig config;

    static {
        config = new SerializeConfig();
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
    }

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 对象转JSON串
     *
     * @param obj 待转换的对象
     * @return JSON串
     */
    public static String toJSON(Object obj) {
        return JSON.toJSONString(obj, config, features);
    }

    /**
     * 不使用特性，对象转JSON串
     *
     * @param object 待转换的对象
     * @return JSON串
     */
    @Deprecated
    public static String toJSONNoFeatures(Object object) {
        return JSON.toJSONString(object, config);
    }

    /**
     * JSON串转对象
     *
     * @param text JSON串
     * @return 转换得到的对象
     */
    public static Object toBean(String text) {
        return JSON.parse(text);
    }

    /**
     * 指定泛型，JSON串转对象
     *
     * @param text  JSON串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 转换得到的对象
     */
    public static <T> T toBean(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    /**
     * JSON串转对象数组
     *
     * @param text JSON串
     * @return 对象数组
     */
    public static Object[] toBeanArray(String text) {
        return toBeanArray(text, null);
    }

    /**
     * 指定泛型，JSON串转对象数组
     *
     * @param text  JSON串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象数组
     */
    public static <T> Object[] toBeanArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz).toArray();
    }

    /**
     * 指定泛型，JSON串转对象列表
     *
     * @param text  JSON串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象数组
     */
    public static <T> List<T> toBeanList(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }

    /**
     * JSON串转化为map
     *
     * @param text JSON串
     * @return Map
     */
    public static Map JSONToMap(String text) {
        return JSONObject.parseObject(text);
    }

    /**
     * 将map转化为string
     *
     * @param map map
     * @return JSON串
     */
    public static String MapToJSON(Map map) {
        return JSONObject.toJSONString(map);
    }

}
