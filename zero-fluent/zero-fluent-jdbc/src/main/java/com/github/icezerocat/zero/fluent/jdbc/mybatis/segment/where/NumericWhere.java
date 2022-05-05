package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.where;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.WhereBase;

import java.util.function.Predicate;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp.IN;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp.NOT_IN;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.toArray;

/**
 * 数字相关的比较
 *
 * @param <WHERE>
 * @param <NQ>
 */
@SuppressWarnings({"unused"})
public interface NumericWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
    > extends ObjectWhere<WHERE, NQ> {
    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(int[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(int[] values, Predicate<int[]> when) {
        return this.apply(args -> when.test(values), IN, toArray(values));
    }


    /**
     * in (values)
     *
     * @param values    条件值
     * @param condition 条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(int[] values, boolean condition) {
        return this.apply(condition, IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(long[] values) {
        return this.apply(IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(long[] values, Predicate<long[]> when) {
        return this.apply(args -> when.test(values), IN, toArray(values));
    }

    /**
     * in (values)
     *
     * @param values    条件值
     * @param condition 条件成立时
     * @return 查询器或更新器
     */
    default WHERE in(long[] values, boolean condition) {
        return this.apply(condition, IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values, Predicate<int[]> when) {
        return this.apply(args -> when.test(values), NOT_IN, toArray(values));
    }


    /**
     * not in (values)
     *
     * @param values    条件值
     * @param condition 条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(int[] values, boolean condition) {
        return this.apply(condition, NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values) {
        return this.apply(NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values, Predicate<long[]> when) {
        return this.apply(args -> when.test(values), NOT_IN, toArray(values));
    }

    /**
     * not in (values)
     *
     * @param values    条件值
     * @param condition 条件成立时
     * @return 查询器或更新器
     */
    default WHERE notIn(long[] values, boolean condition) {
        return this.apply(condition, NOT_IN, toArray(values));
    }
}
