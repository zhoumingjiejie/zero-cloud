package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.where;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.QFunction;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.ifs.Ifs;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.WhereBase;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp.*;

/**
 * ObjectWhere
 *
 * @param <WHERE> WHERE
 * @param <NQ>    子查询
 * @author wudarui
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public interface ObjectWhere<
    WHERE extends WhereBase<WHERE, ?, NQ>,
    NQ extends IBaseQuery<?, NQ>
    > extends BaseWhere<WHERE, NQ> {

    /**
     * 大于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE gt(Object value) {
        return this.apply(GT, value);
    }

    /**
     * 大于
     *
     * @param value     条件值
     * @param condition 条件
     * @return 查询器或更新器
     */
    default WHERE gt(Object value, boolean condition) {
        return this.apply(condition, GT, value);
    }

    /**
     * 大于
     *
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE gt(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), GT, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if condition
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE gt(Ifs<T> ifs) {
        return this.apply(GT, ifs);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE ge(Object value) {
        return this.apply(GE, value);
    }

    /**
     * 大于等于
     *
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE ge(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), GE, value);
    }

    /**
     * 大于等于
     *
     * @param value     条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE ge(T value, boolean condition) {
        return this.apply(condition, GE, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE ge(Ifs<T> ifs) {
        return this.apply(GE, ifs);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE lt(Object value) {
        return this.apply(LT, value);
    }

    /**
     * 小于
     *
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE lt(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), LT, value);
    }

    /**
     * 小于
     *
     * @param value     条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE lt(T value, boolean condition) {
        return this.apply(condition, LT, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE lt(Ifs<T> ifs) {
        return this.apply(LT, ifs);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @return 查询器或更新器
     */
    default WHERE le(Object value) {
        return this.apply(LE, value);
    }

    /**
     * 小于等于
     *
     * @param value 条件值
     * @param when  为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE le(T value, Predicate<T> when) {
        return this.apply(args -> when.test(value), LE, value);
    }

    /**
     * 小于等于
     *
     * @param value     条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE le(T value, boolean condition) {
        return this.apply(condition, LE, value);
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @param <T> type
     * @return WHERE
     */
    default <T> WHERE le(Ifs<T> ifs) {
        return this.apply(LE, ifs);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE in(Object[] values) {
        return this.apply(IN, values);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE in(T[] values, Predicate<T[]> when) {
        return this.apply(args -> when.test(values), IN, values);
    }

    /**
     * in (values)
     *
     * @param values    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE in(T[] values, boolean condition) {
        return this.apply(condition, IN, values);
    }

    /**
     * in (values)
     *
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default WHERE in(Collection values, Predicate<Collection> when) {
        return this.apply(args -> when.test(values), IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * in (values)
     *
     * @param values    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default WHERE in(Collection values, boolean condition) {
        return this.apply(condition, IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * 按Ifs条件设置where值
     *
     * @param ifs if conditions
     * @return WHERE
     */
    default WHERE in(Ifs<Collection> ifs) {
        return this.apply(IN, ifs);
    }

    /**
     * where column IN (select ... )
     *
     * @param select 子查询语句
     * @param args   子查询语句参数，对应select语句里面的 "?" 占位符
     * @return 查询器或更新器
     */
    <O> WHERE in(String select, O... args);

    /**
     * where column IN (select ... )
     *
     * @param condition true时条件成立
     * @param select    子查询语句
     * @param args      子查询语句参数，对应select语句里面的 "?" 占位符
     * @param <O>       type
     * @return 查询器或更新器
     */
    <O> WHERE in(boolean condition, String select, O... args);

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(QFunction<NQ> query);

    /**
     * in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(IQuery query);

    /**
     * in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, QFunction<NQ> query);

    /**
     * in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE in(boolean condition, IQuery query);

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(Object[] values) {
        return this.apply(NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notIn(T[] values, Predicate<T[]> when) {
        return this.apply(args -> when.test(values), NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param values    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notIn(T[] values, boolean condition) {
        return this.apply(condition, NOT_IN, values);
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @return 查询器或更新器
     */
    default WHERE notIn(Collection values) {
        return this.apply(NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (values)
     *
     * @param values 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default WHERE notIn(Collection values, Predicate<Collection> when) {
        return this.apply(args -> when.test(values), NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (values)
     *
     * @param values    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default WHERE notIn(Collection values, boolean condition) {
        return this.apply(condition, NOT_IN, values == null ? new Object[0] : values.toArray());
    }

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(QFunction<NQ> query);

    /**
     * not in (select ... )
     *
     * @param query 嵌套查询
     * @return WHERE
     */
    WHERE notIn(IQuery query);

    /**
     * not in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, QFunction<NQ> query);

    /**
     * not in (select ... )
     *
     * @param condition true时条件成立
     * @param query     嵌套查询
     * @return 查询器或更新器
     */
    WHERE notIn(boolean condition, IQuery query);

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE between(T value1, T value2) {
        return this.apply(BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE between(T value1, T value2, BiPredicate<T, T> when) {
        return this.apply(args -> when.test(value1, value2), BETWEEN, value1, value2);
    }

    /**
     * @param value1    条件值
     * @param value2    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE between(T value1, T value2, boolean condition) {
        return this.apply(condition, BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @return 查询器或更新器
     */
    default <T> WHERE notBetween(T value1, T value2) {
        return this.apply(NOT_BETWEEN, value1, value2);
    }

    /**
     * @param value1 条件值
     * @param value2 条件值
     * @param when   为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notBetween(T value1, T value2, BiPredicate<T, T> when) {
        return this.apply(args -> when.test(value1, value2), NOT_BETWEEN, value1, value2);
    }

    /**
     * @param value1    条件值
     * @param value2    条件值
     * @param condition 为真时成立
     * @return 查询器或更新器
     */
    default <T> WHERE notBetween(T value1, T value2, boolean condition) {
        return this.apply(condition, NOT_BETWEEN, value1, value2);
    }
}
