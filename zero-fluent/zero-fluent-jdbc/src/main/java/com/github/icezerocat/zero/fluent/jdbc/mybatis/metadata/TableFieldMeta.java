package com.github.icezerocat.zero.fluent.jdbc.mybatis.metadata;

import com.github.icezerocat.zero.fluent.annotation.mybatis.annotation.TableField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.isBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.notBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.camelToUnderline;

/**
 * 数据库表字段反射信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TableFieldMeta extends FieldMeta {
    /**
     * numericScale
     */
    protected String numericScale;
    /**
     * 是否非大字段查询
     * <p>大字段可设置为 false 不加入 select 查询范围</p>
     */
    protected boolean notLarge = true;
    /**
     * 字段 update set 部分注入
     */
    protected String update;
    /**
     * 字段填充策略
     */
    protected String insert;

    /**
     * 全新的 存在 TableField 注解时使用的构造函数
     */
    public TableFieldMeta(Field field, TableField tableField) {
        super(tableField == null || isBlank(tableField.value()) ? camelToUnderline(field.getName(), false) : tableField.value(), field);
        if (tableField != null) {
            this.setJdbcType(tableField.jdbcType());
            this.numericScale = tableField.numericScale();
            this.typeHandler = UnknownTypeHandler.class == tableField.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableField.typeHandler();

            this.notLarge = tableField.notLarge();
            this.insert = tableField.insert();
            this.update = tableField.update();
        }
    }

    @Override
    protected String el() {
        String el = super.el();
        if (notBlank(numericScale)) {
            el += (", numericScale = " + numericScale);
        }
        return el;
    }
}
