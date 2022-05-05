package com.github.icezerocat.zero.fluent.annotation.form.annotation;

import java.lang.annotation.*;

/**
 * 标识Form Object
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Form {
    /**
     * 和前置条件关联方式, true: 以and方式; false: 以or方式
     */
    boolean and() default true;

    /**
     * Form实体描述
     */
    String desc() default "";
}
