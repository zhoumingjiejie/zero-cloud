package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;

/**
 * IEntityQuery: 查询接口
 *
 * @param <E> 对应的实体类
 * @param <Q> 查询器
 * @author darui.wu
 */
public interface IBaseQuery<
    E extends IEntity,
    Q extends IBaseQuery<E, Q>>
    extends IWrapper<E, Q, Q>, IQuery<E> {
}
