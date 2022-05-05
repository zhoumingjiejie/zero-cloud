package com.github.icezerocat.zero.fluent.annotation.mybatis.annotation;

import org.apache.ibatis.type.JdbcType;

import java.lang.annotation.*;

/**
 * 表主键标识
 *
 * @author darui.wu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {
    /**
     * 数据库字段名称
     * 默认遵循 驼峰命名到下划线命名的转换规则
     */
    String value() default "";

    /**
     * 是否自增主键
     */
    boolean auto() default true;

    /**
     * JDBC类型 (该默认值不代表会按照该值生效)
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 自增主键产生的sequence name
     */
    String seqName() default "";

    /**
     * 只在seqName有值时才有效
     */
    boolean before() default false;
    /**
     * 字段描述
     */
    String desc() default "";
}
