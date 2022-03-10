package com.github.icezerocat.zero.jdbc.builder.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dragons
 * @date 2021/12/14 10:09
 */
public class MapperFactoryBean<T> implements FactoryBean<T>, EnvironmentAware {

    private Class<T> mapperInterface;

    private Environment environment;

    private String dataSourceConfigPrefix;

    private static final Map<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();

    public MapperFactoryBean() {
        // intentionally empty
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        Class<?> modelClass = getActualTypeArguments0();
        T bean = (T) Proxy.newProxyInstance(
            getClass().getClassLoader(), new Class<?>[]{mapperInterface}, new JdbcBaseMapperJdkProxyHandler(modelClass, jdbcTemplate)
        );
        return bean;
    }

    private JdbcTemplate getJdbcTemplate() {
        String url = Objects.requireNonNull(environment.getProperty(dataSourceConfigPrefix + ".url"));
        String username = environment.getProperty(dataSourceConfigPrefix + ".username");
        String password = environment.getProperty(dataSourceConfigPrefix + ".password");
        String driverClassName = Objects.requireNonNull(environment.getProperty(dataSourceConfigPrefix + ".driver-class-name", environment.getProperty(dataSourceConfigPrefix + ".driverClassName")));

        String key = url + "`" + username + "`" + password + "`" + driverClassName;
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(key);
        if (jdbcTemplate == null) {
            synchronized (jdbcTemplateMap) {
                jdbcTemplate = jdbcTemplateMap.get(key);
                if (jdbcTemplate == null) {
                    jdbcTemplate = new JdbcTemplate(
                        DataSourceBuilder.create()
                            .url(url)
                            .driverClassName(driverClassName)
                            .username(username)
                            .password(password)
                            .build()
                    );
                    jdbcTemplateMap.put(key, jdbcTemplate);
                }
            }
        }
        return jdbcTemplate;
    }


    private Class<?> getActualTypeArguments0() {
        Class<?> clazz = mapperInterface;
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
            }
        }
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setDataSourceConfigPrefix(String dataSourceConfigPrefix) {
        this.dataSourceConfigPrefix = dataSourceConfigPrefix;
    }


    public String getDataSourceConfigPrefix() {
        return dataSourceConfigPrefix;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
