package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IAggregate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.FormatFrag;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;

/**
 * 聚合函数枚举
 *
 * @author wudarui
 */
public enum Aggregate implements IAggregate {
    /**
     * 求总和
     */
    SUM("SUM(%s)"),
    /**
     * 求总数
     */
    COUNT("COUNT(%s)"),
    /**
     * 求最大值
     */
    MAX("MAX(%s)"),
    /**
     * 求最小值
     */
    MIN("MIN(%s)"),
    /**
     * 求平均值
     */
    AVG("AVG(%s)"),
    /**
     * 组内连接
     */
    GROUP_CONCAT("GROUP_CONCAT(%s)");

    private final String expression;

    Aggregate(String expression) {
        this.expression = expression;
    }

    @Override
    public IFragment aggregate(IFragment column) {
        return FormatFrag.format(expression, column);
    }
}
