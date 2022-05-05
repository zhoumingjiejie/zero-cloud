package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.mapper;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.annotation.FluentMybatis;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IDefaultSetter;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IUpdate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.UniqueType;

/**
 * 以下方法在EntityMapper中实现接口default方法
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface IWrapperMapper<E extends IEntity, Q extends IQuery<E>, U extends IUpdate<E>>
    extends IEntityMapper<E>, IRichMapper<E> {

    /**
     * 返回对应的默认构造器
     *
     * @return IMapping
     */
    IMapping mapping();

    /**
     * 构造空查询条件
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setQueryDefault(IQuery)}
     *
     * @return ignore
     */
    default Q query() {
        return mapping().query();
    }

    default Q emptyQuery() {
        return mapping().emptyQuery();
    }

    /**
     * 构造设置了默认条件的Updater
     * 默认条件设置{@link FluentMybatis#defaults()}, 具体定义继承 {@link IDefaultSetter#setUpdateDefault(IUpdate)}
     *
     * @return ignore
     */
    default U updater() {
        return mapping().updater();
    }

    /**
     * 构造空更新条件
     *
     * @return ignore
     */
    default U emptyUpdater() {
        return mapping().emptyUpdater();
    }

    /**
     * 主键字段名称
     *
     * @return ignore
     */
    default FieldMapping primaryField() {
        return this.mapping().findField(UniqueType.PRIMARY_ID)
            .orElseThrow(() -> new RuntimeException("primary key not found."));
    }

    /**
     * 对应的entity class类
     *
     * @return ignore
     */
    default Class<E> entityClass() {
        return (Class<E>) mapping().entityClass();
    }
}
