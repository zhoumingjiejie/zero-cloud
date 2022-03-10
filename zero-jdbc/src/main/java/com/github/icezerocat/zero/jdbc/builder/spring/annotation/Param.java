package com.github.icezerocat.zero.jdbc.builder.spring.annotation;

import java.lang.annotation.*;

/**
 * @author dragons
 * @date 2021/12/13 9:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Documented
public @interface Param {
    String value() default "";
}
