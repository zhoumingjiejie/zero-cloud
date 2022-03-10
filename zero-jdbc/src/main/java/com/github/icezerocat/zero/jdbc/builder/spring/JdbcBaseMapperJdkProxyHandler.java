package com.github.icezerocat.zero.jdbc.builder.spring;

import com.github.icezerocat.zero.jdbc.builder.spring.util.MapperUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author dragons
 * @date 2021/12/10 16:40
 */
public class JdbcBaseMapperJdkProxyHandler implements InvocationHandler {

    private final JdbcBaseMapperHandler handler;

    public JdbcBaseMapperJdkProxyHandler(Class<?> beanClass, JdbcTemplate jdbcTemplate) {
        handler = new JdbcBaseMapperHandler(beanClass, jdbcTemplate, MapperUtils.getMapper(beanClass));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = handler.handle(method, args);
        if (result == null || method.getReturnType() == Void.TYPE) {
            result = Void.class;
        }
        return result;
    }
}
