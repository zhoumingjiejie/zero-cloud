package com.github.icezerocat.zero.fluent.jdbc.form.registrar;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.spring.MapperFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {

    private final Class serviceClass;

    private final Class aroundClass;

    public FormServiceFactoryBean(Class serviceClass, Class aroundClass) {
        this.serviceClass = serviceClass;
        this.aroundClass = aroundClass;
    }

    /**
     * 确保 FormServiceFactoryBean 依赖于 {@link MapperFactory}
     */
    @SuppressWarnings("all")
    @Autowired
    public void setMapperFactory(MapperFactory factory) {
        // do nothing
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private volatile Object proxy;

    @Override
    public Object getObject() {
        if (this.proxy == null) {
            /* 为避免在MapperFactory.init()方法前执行proxy实例化,
            proxy实例化放到getObject()中执行, 不能放构造函数中执行*/
            synchronized (this) {
                if (this.proxy == null) {
                    this.proxy = FormServiceProxy.create(serviceClass, aroundClass);
                }
            }
        }
        return this.proxy;
    }
}
