package com.github.icezerocat.zero.fluent.jdbc.mybatis.functions;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;

/**
 * 聚合接口
 *
 * @author wudarui
 */
@FunctionalInterface
public interface IAggregate {
    /**
     * 聚合函数表达式
     *
     * @param column 聚合字段
     * @return 聚合表达式
     */
    IFragment aggregate(IFragment column);

    default IFragment aggregate(String column) {
        return this.aggregate(Column.set(column));
    }
}
