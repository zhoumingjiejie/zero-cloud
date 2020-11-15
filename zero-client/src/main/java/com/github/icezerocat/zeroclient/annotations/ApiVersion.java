package com.github.icezerocat.zeroclient.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 接口版本管理注解
 * CreateDate:  2020/11/2 15:09
 *
 * @author zero
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiVersion {

    /**
     * 接口版本号(对应swagger中的group)
     *
     * @return String[]
     */
    String[] value() default {};

}

