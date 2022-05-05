package com.github.icezerocat.zero.fluent.annotation.form.annotation;

import com.github.icezerocat.zero.fluent.annotation.form.registrar.FormServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FormServiceRegistrar.RepeatingRegistrar.class)
public @interface FormServiceScans {
    FormServiceScan[] value();
}
