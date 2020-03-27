package net.unmz.java.util.bean;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Project Name:
 * 功能描述：
 * 参考链接1: https://blog.csdn.net/y526089989/article/details/89389087
 * 参考链接2: https://blog.csdn.net/wengerwss/article/details/52597627
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2020/3/27 16:10
 * @since JDK 1.8
 */
public class ClassUtils {


    /**
     * @param object    旧的对象带值
     * @param addMap    动态需要添加的属性和属性类型
     * @param addValMap 动态需要添加的属性和属性值
     * @return 新的对象
     * @throws Exception
     */
    public static Object dynamicClass(Object object, HashMap<String, Object> addMap, HashMap<String, Object> addValMap) throws Exception {
        HashMap<String, Object> returnMap = new HashMap<>();
        HashMap<String, Object> typeMap = new HashMap<>();

        Class<?> type = object.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(object);
                //可以判断为 NULL不赋值
                returnMap.put(propertyName, result);
                typeMap.put(propertyName, descriptor.getPropertyType());
            }
        }

        returnMap.putAll(addValMap);
        typeMap.putAll(addMap);
        //map转换成实体对象
        DynamicBean bean = new DynamicBean(typeMap);
        //赋值
        Set<String> keys = typeMap.keySet();
        for (String key : keys) {
            bean.setValue(key, returnMap.get(key));
        }
        return bean.getObject();
    }

    static class DynamicBean {
        private Object object = null; //动态生成的类
        private BeanMap beanMap = null; //存放属性名称以及属性的类型

        public DynamicBean() {
            super();
        }

        public DynamicBean(Map propertyMap) {
            this.object = generateBean(propertyMap);
            this.beanMap = BeanMap.create(this.object);
        }

        /**
         * @param propertyMap
         * @return
         */
        private Object generateBean(Map propertyMap) {
            BeanGenerator generator = new BeanGenerator();
            Set keySet = propertyMap.keySet();
            for (Iterator i = keySet.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                generator.addProperty(key, (Class) propertyMap.get(key));
            }
            return generator.create();
        }

        /**
         * 给bean属性赋值
         *
         * @param property 属性名
         * @param value    值
         */
        public void setValue(Object property, Object value) {
            beanMap.put(property, value);
        }

        /**
         * 通过属性名得到属性值
         *
         * @param property 属性名
         * @return 值
         */
        public Object getValue(String property) {
            return beanMap.get(property);
        }

        /**
         * 得到该实体bean对象
         *
         * @return
         */
        public Object getObject() {
            return this.object;
        }
    }
}

