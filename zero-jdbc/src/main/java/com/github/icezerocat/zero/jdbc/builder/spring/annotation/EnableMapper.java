package com.github.icezerocat.zero.jdbc.builder.spring.annotation;

import com.github.icezerocat.zero.jdbc.builder.spring.MapperScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author dragons
 * @date 2021/12/13 12:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Import(MapperScannerRegistrar.class)
@Documented
public @interface EnableMapper {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    /**
     * @return 返回配置前缀
     * 数据源配置项
     * 必填项:
     * ${dataSourceConfigPrefix}.url: jdbc connection url
     * ${dataSourceConfigPrefix}.username
     * ${dataSourceConfigPrefix}.password
     * ${dataSourceConfigPrefix}.driver-class-name
     */
    String dataSourceConfigPrefix() default "spring.datasource";
}
