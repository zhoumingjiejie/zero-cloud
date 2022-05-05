package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IWrapper;

/**
 * OrderByApply: 排序方式
 *
 * @param <O> 排序操作
 * @param <W> 查询器或更新器
 */
@SuppressWarnings({"unused"})
public class OrderByApply<
    O extends OrderByBase<O, W>,
    W extends IWrapper<?, W, ?>
    > extends BaseApply<O, W> {

    OrderByApply(O segment) {
        super(segment);
    }

    /**
     * 按照正序排
     *
     * @return ignore
     */
    public O asc() {
        this.segment.applyField(this.current(), true);
        return this.segment;
    }

    /**
     * 按照逆序排
     *
     * @return ignore
     */
    public O desc() {
        this.segment.applyField(this.current(), false);
        return this.segment;
    }

    /**
     * 如果条件成立，按照字段正序排，否则丢弃
     *
     * @param condition 成立条件
     * @return ignore
     */
    public O ascIf(boolean condition) {
        if (condition) {
            this.segment.applyField(this.current(), true);
        }
        return this.segment;
    }

    /**
     * 如果条件成立，按照字段逆序排，否则丢弃
     *
     * @param condition 成立条件
     * @return GroupBy排序器
     */
    public O descIf(boolean condition) {
        if (condition) {
            this.segment.applyField(this.current(), false);
        }
        return this.segment;
    }
}
