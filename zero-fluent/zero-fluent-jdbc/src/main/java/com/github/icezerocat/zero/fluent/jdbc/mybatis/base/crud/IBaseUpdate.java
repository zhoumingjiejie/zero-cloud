package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;

/**
 * IEntityUpdate: 更新接口
 *
 * @param <E>  对应的实体类
 * @param <U>  更新器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IBaseUpdate<
    E extends IEntity,
    U extends IBaseUpdate<E, U, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends IWrapper<E, U, NQ>, IUpdate<E> {
}
