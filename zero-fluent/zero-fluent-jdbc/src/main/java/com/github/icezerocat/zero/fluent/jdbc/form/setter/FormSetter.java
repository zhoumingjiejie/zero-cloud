package com.github.icezerocat.zero.fluent.jdbc.form.setter;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;

import java.util.function.Consumer;

/**
 * 字段设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public class FormSetter {
    private final IMapping mapping;

    protected FormApply formApply;

    protected Consumer<FieldMapping> apply;

    public FormSetter(IMapping mapping) {
        this.mapping = mapping;
    }

    public void set(Consumer<FieldMapping> apply) {
        this.apply = apply;
    }

    /**
     * 设置当前操作字段
     *
     * @param field 字段
     * @return ignore
     */
    public IFormApply set(FieldMapping field) {
        this.formApply.addWhere(field);
        return this.formApply;
    }

    public Class entityClass() {
        return mapping.entityClass();
    }

    public IMapping _mapping() {
        return mapping;
    }
}
