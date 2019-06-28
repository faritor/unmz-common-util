package net.unmz.java.util.asserts;

/**
 * Project Name:
 * 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2019-1-10 17:03
 * @since JDK 1.8
 */
public class Assert {

    public static void isEmpty(Object object) {
        if (object != null) {
            throw new IllegalArgumentException("object is not empty");
        }
    }

    public static void isNotEmpty(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("object is empty");
        }
    }

    public static void equals(Object object1, Object object2) {
        isNotEmpty(object1);
        isNotEmpty(object2);

        if (object1.hashCode() != object2.hashCode()) {
            throw new IllegalArgumentException(" object is not equals ");
        }

    }

}
