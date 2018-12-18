package net.unmz.java.util.map;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: 常用工具类集合
 * 功能描述：Map工具箱
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-12 11:14
 * @since JDK 1.8
 */
public class MapUtils {

    /**
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, String> objectToMap(Object obj) throws Exception {
        if (null != obj) {
            Map<String, String> map = new HashMap();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                String value = null;
                if (getter != null)
                    value = getter.invoke(obj) != null ? getter.invoke(obj).toString() : null;
                map.put(key, value);
            }
            return map;
        } else {
            return null;
        }
    }

    /**
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map map, Class<?> beanClass) throws Exception {
        if (null != map) {
            return null;
        }
        Object obj = beanClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }
        return obj;
    }

}
