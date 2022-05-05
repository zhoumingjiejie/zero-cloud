package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.where;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.WhereBase;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp.EQ;

/**
 * 布尔值比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
@SuppressWarnings({"unused"})
public interface BooleanWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
    > extends BaseWhere<WHERE, NQ> {
    /**
     * 等于 true
     *
     * @return 查询器或更新器
     */
    default WHERE isTrue() {
        return this.apply(EQ, Boolean.TRUE);
    }

    /**
     * 等于 false
     *
     * @return 查询器或更新器
     */
    default WHERE isFalse() {
        return this.apply(EQ, Boolean.FALSE);
    }
}
