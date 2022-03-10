package com.github.icezerocat.zero.jdbc.builder.spring.annotation;

import java.lang.annotation.*;

/**
 * @author dragons
 * @date 2021/12/10 18:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Update {
    /**
     * sql
     */
    String value() default "";
}
