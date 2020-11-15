package com.github.icezerocat.zeroclient.config;

import com.github.icezerocat.zeroclient.annotations.ApiVersion;
import com.github.icezerocat.zeroclient.annotations.ApiVersionConstant;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.github.xiaoymin.swaggerbootstrapui.model.SpringAddtionalModel;
import com.github.xiaoymin.swaggerbootstrapui.service.SpringAddtionalModelService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Description: Swagger配置
 * CreateDate:  2020/4/7 14:38
 *
 * @author 0.0.0
 * @version 1.0
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig implements InitializingBean {

    @Resource
    private SwaggerProperties swaggerProperties;
    @Resource
    private ApplicationContext applicationContext;

    private final SpringAddtionalModelService springAddtionalModelService;

    public SwaggerConfig(SpringAddtionalModelService springAddtionalModelService) {
        this.springAddtionalModelService = springAddtionalModelService;
    }

    /**
     * 创建接口api
     *
     * @return 案卷（创建结果）
     */
    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        this.scanModels(docket);
        return docket;
    }

    /**
     * 动态得创建Docket bean
     *
     * @throws Exception bean操作异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // ApiConstantVersion 里面定义的每个变量会成为一个docket
        Class<ApiVersionConstant> clazz = ApiVersionConstant.class;
        Field[] declaredFields = clazz.getDeclaredFields();

        // 动态注入bean
        AutowireCapableBeanFactory autowireCapableBeanFactory = this.applicationContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory capableBeanFactory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
            for (Field declaredField : declaredFields) {
                // 要注意 "工厂名和方法名"，意思是用这个bean的指定方法创建docket
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                        .genericBeanDefinition()
                        .setFactoryMethodOnBean("buildDocket", "swaggerConfig")
                        .addConstructorArgValue(declaredField.get(ApiVersionConstant.class)).getBeanDefinition();
                capableBeanFactory.registerBeanDefinition(declaredField.getName(), beanDefinition);
            }
        }
    }

    /**
     * 构建docket,方法上的优先级大于 controller上的版本注解优先级
     *
     * @param groupName 组名
     * @return docket
     */
    @SuppressWarnings("unused")
    private Docket buildDocket(String groupName) {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.groupName(groupName)
                .select()
                .apis(method -> {
                    // 每个方法会进入这里进行判断并归类到不同分组
                    if (method != null) {
                        HandlerMethod handlerMethod = method.getHandlerMethod();
                        // 该方法上标注了版本
                        if (method.isAnnotatedWith(ApiVersion.class)) {
                            ApiVersion apiVersion = handlerMethod.getMethodAnnotation(ApiVersion.class);
                            if (apiVersion != null && apiVersion.value().length != 0) {
                                return Arrays.asList(apiVersion.value()).contains(groupName);
                            }
                        }

                        // 方法所在的类是否标注了?
                        ApiVersion annotationOnClass = handlerMethod.getBeanType().getAnnotation(ApiVersion.class);
                        if (annotationOnClass != null && annotationOnClass.value().length != 0) {
                            return Arrays.asList(annotationOnClass.value()).contains(groupName);
                        }
                        return false;
                    }
                    return false;
                })
                .paths(PathSelectors.any())
                .build();
        this.scanModels(docket);
        return docket;
    }

    /**
     * 扫描model
     *
     * @param docket 案卷
     */
    private void scanModels(Docket docket) {
        //自定义扫描models包
        String[] basePackages = this.swaggerProperties.getBasePackages();
        if (basePackages != null) {
            SpringAddtionalModel springAddtionalModel = this.springAddtionalModelService.scan(basePackages);
            docket.additionalModels(springAddtionalModel.getFirst(), springAddtionalModel.getRemaining());
        }
    }
}
