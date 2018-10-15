package net.unmz.java.util.data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Project Name:
 * 功能描述： 本文方法节选自网络,略有修改
 * 原文地址:https://blog.csdn.net/lupengfei1009/article/details/80540479
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018-10-15 11:42
 * @since JDK 1.8
 */
public class DataLengthCheckHelper {

    /**
     * 校验数据属性至
     *
     * @param obj
     * @throws IllegalArgumentException
     */
    public static void validateAttributeValueLength(Object obj) throws IllegalArgumentException {
        if (null != obj) {
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {// 遍历
                try {
                    Field field = fields[i];
                    Annotation[] annotations = field.getAnnotations();
                    ValidateLength dataLength = null;
                    for (Annotation annotation : annotations)
                        if (annotation instanceof ValidateLength)
                            dataLength = (ValidateLength) annotation;

                    if (null != dataLength) {
                        field.setAccessible(true); // 打开私有访问
                        String name = field.getName();// 获取属性
                        Object value = field.get(obj);// 获取属性值
                        if (value == null && !dataLength.nullable())
                            throw new IllegalArgumentException(name + " is null");
                        int length = dataLength.value();// 指定的长度
                        int valueLength = 0;// 数据的长度
                        String data;
                        if (value instanceof String) {// 一个个赋值
                            data = (String) value;
                            valueLength = data.length();
                        }

                        if (valueLength > length)
                            throw new IllegalArgumentException(name + " length:" + valueLength + " too long");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
