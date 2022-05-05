package com.github.icezerocat.zero.fluent.annotation.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 版本锁字段标识
 *
 * @author wudarui
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Version {
}
