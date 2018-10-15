package net.unmz.java.util.data;

import java.lang.annotation.*;

/**
 * Project Name:
 * 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018-10-15 11:41
 * @since JDK 1.8
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateLength {

    int value() default Integer.MAX_VALUE;

    boolean nullable() default true;
}
