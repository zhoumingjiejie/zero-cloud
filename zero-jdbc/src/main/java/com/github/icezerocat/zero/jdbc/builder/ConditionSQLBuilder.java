package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.annotation.Queries;
import com.github.icezerocat.zero.jdbc.builder.annotation.Query;
import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;
import com.github.icezerocat.zero.jdbc.builder.util.CastUtils;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/11 19:42
 */
public class ConditionSQLBuilder<T extends ConditionSQLBuilder> implements SQLBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected final String prefix;

    protected final StringBuilder conditionBuilder;

    protected String sign = "WHERE";

    protected List<Object> precompileArgs = new ArrayList<>();

    protected int level = 2;

    protected ConditionSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, T b) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        if (Boolean.TRUE.equals(predicate)) {
            if (b != null) {
                this.conditionBuilder = b.conditionBuilder;
                this.sign = b.sign;
                this.precompileArgs.addAll(b.precompileArgs);
                this.level = b.level;
            } else {
                this.conditionBuilder = new StringBuilder();
            }
        } else {
            this.conditionBuilder = new StringBuilder();
        }
    }

    protected ConditionSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs) {
        this.prefix = prefix;
        this.conditionBuilder = new StringBuilder();
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
    }

    protected ConditionSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Object ...values) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        if (Boolean.TRUE.equals(predicate)) {
            this.conditionBuilder = new StringBuilder(condition);
            if (condition.contains("OR")) {
                level = 1;
            } else if (condition.contains("AND")) {
                level = 2;
            }
            this.precompileArgs.addAll(Arrays.asList(values));
        } else {
            this.conditionBuilder = new StringBuilder();
        }
    }

    protected ConditionSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Supplier<Object[]> supplier) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        if (Boolean.TRUE.equals(predicate)) {
            this.conditionBuilder = new StringBuilder(condition);
            if (condition.contains("OR")) {
                level = 1;
            } else if (condition.contains("AND")) {
                level = 2;
            }
            this.precompileArgs.addAll(Arrays.asList(supplier.get()));
        } else {
            this.conditionBuilder = new StringBuilder();
        }
    }

    protected ConditionSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, Object ...queryCriteria) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        if (Boolean.TRUE.equals(predicate)) {
            T builder = conditionQueryCriteria(queryCriteria);
            if (builder != null) {
                this.conditionBuilder = builder.conditionBuilder;
                this.sign = builder.sign;
                this.precompileArgs.addAll(builder.precompileArgs);
                this.level = builder.level;
            } else {
                this.conditionBuilder = new StringBuilder();
            }
        } else {
            this.conditionBuilder = new StringBuilder();
        }
    }


    public T and(String condition, Object ...values) {
        if (condition != null && condition.length() > 0) {
            if (conditionBuilder.length() != 0) {
                condition = " AND " + condition;
            }
            addCondition(condition, 2);
            if (values.length > 0) {
                precompileArgs.addAll(Arrays.asList(values));
            }
        }
        return (T) this;
    }

    private T and(String condition, List<Object> values) {
        if (condition != null && condition.length() > 0) {
            if (conditionBuilder.length() != 0) {
                condition = " AND " + condition;
            }
            addCondition(condition, 2);
            if (values != null && !values.isEmpty()) {
                precompileArgs.addAll(values);
            }
        }
        return (T) this;
    }

    public T and(String column, Operator option, Object ...values) {
        Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
        return and(pt._1, pt._2);
    }

    public T and(T wrapper) {
        if (wrapper != null) {
            return (T) and("(" + wrapper.conditionBuilder.toString() + ")", wrapper.precompileArgs);
        }
        return (T) this;
    }

    public T and(Object ...queryCriteria) {
        if (queryCriteria.length > 0) {
            return and(conditionQueryCriteria(queryCriteria));
        }
        return (T)this;
    }

    public T and(Boolean predicate, String column, Operator option, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
            return and(pt._1, pt._2);
        }
        return (T) this;
    }

    public T and(Boolean predicate, String column, Operator option, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
            return and(pt._1, pt._2);
        }
        return (T) this;
    }

    public T and(Boolean predicate, String condition, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(condition, values);
        }
        return (T) this;
    }

    public T and(Boolean predicate, Supplier<String> condition, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(condition.get(), values);
        }
        return (T) this;
    }

    public T and(Boolean predicate, String condition, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(condition, values.get());
        }
        return (T) this;
    }

    public T and(Boolean predicate, Supplier<String> condition, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(condition.get(), values.get());
        }
        return (T) this;
    }

    public T and(Boolean predicate, T wrapper) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(wrapper);
        }
        return (T) this;
    }

    public T and(Boolean predicate, Supplier<T> wrapper) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(wrapper.get());
        }
        return (T) this;
    }

    public T and(Boolean predicate, Object ...queryCriteria) {
        if (predicate) {
            return and(queryCriteria);
        }
        return (T) this;
    }

    public T or(String condition, Object ...values) {
        if (condition != null && condition.length() > 0) {
            if (conditionBuilder.length() != 0) {
                condition = " OR " + condition;
            }
            addCondition(condition, 1);
            if (values.length > 0) {
                precompileArgs.addAll(Arrays.asList(values));
            }
        }
        return (T) this;
    }

    private T or(String condition, List<Object> values) {
        if (condition != null && condition.length() > 0) {
            if (conditionBuilder.length() != 0) {
                condition = " OR " + condition;
            }
            addCondition(condition, 1);
            if (values != null && !values.isEmpty()) {
                precompileArgs.addAll(values);
            }
        }
        return (T) this;
    }

    public T or(String column, Operator option, Object ...values) {
        Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
        return or(pt._1, pt._2);
    }

    public T or(String column, Operator option, Supplier<Object[]> values) {
        Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
        return or(pt._1, pt._2);
    }

    public T or(T wrapper) {
        if (wrapper != null) {
            return (T) or("(" + wrapper.conditionBuilder.toString() + ")", wrapper.precompileArgs);
        }
        return (T) this;
    }

    public T or(Object ...queryCriteria) {
        if (queryCriteria.length > 0) {
            return or(conditionQueryCriteria(queryCriteria));
        }
        return (T) this;
    }

    public T or(Boolean predicate, String column, Operator option, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
            return or(pt._1, pt._2);
        }
        return (T) this;
    }

    public T or(Boolean predicate, String column, Operator option, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
            return or(pt._1, pt._2);
        }
        return (T) this;
    }

    public T or(Boolean predicate, String condition, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(condition, values);
        }
        return (T) this;
    }

    public T or(Boolean predicate, Supplier<String> condition, Object ...values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(condition.get(), values);
        }
        return (T) this;
    }

    public T or(Boolean predicate, String condition, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(condition, values.get());
        }
        return (T) this;
    }

    public T or(Boolean predicate, Supplier<String> condition, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(condition.get(), values.get());
        }
        return (T) this;
    }

    public T or(Boolean predicate, T wrapper) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(wrapper);
        }
        return (T) this;
    }

    public T or(Boolean predicate, Supplier<T> wrapper) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(wrapper.get());
        }
        return (T) this;
    }

    public T or(Boolean predicate, Object ...queryCriteria) {
        if (predicate) {
            return or(queryCriteria);
        }
        return (T) this;
    }

    public T andEq(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.EQ, value.get());
        }
        return (T) this;
    }

    public T andEq(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.EQ, value);
    }

    public T andEq(String column, Object value) {
        return and(column, Operator.EQ, value);
    }

    public T andGt(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.GT, value.get());
        }
        return (T) this;
    }

    public T andGt(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.GT, value);
    }

    public T andGt(String column, Object value) {
        return and(column, Operator.GT, value);
    }

    public T andGe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.GE, value.get());
        }
        return (T) this;
    }

    public T andGe(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.GE, value);
    }

    public T andGe(String column, Object value) {
        return and(column, Operator.GE, value);
    }

    public T andLt(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.LT, value.get());
        }
        return (T) this;
    }

    public T andLt(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.LT, value);
    }

    public T andLt(String column, Object value) {
        return and(column, Operator.LT, value);
    }

    public T andLe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.LE, value.get());
        }
        return (T) this;
    }

    public T andLe(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.LE, value);
    }

    public T andLe(String column, Object value) {
        return and(column, Operator.LE, value);
    }

    public T andNe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.NE, value.get());
        }
        return (T) this;
    }

    public T andNe(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.NE, value);
    }

    public T andNe(String column, Object value) {
        return and(column, Operator.NE, value);
    }

    public T andNe2(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.LTGT, value.get());
        }
        return (T) this;
    }

    public T andNe2(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.LTGT, value);
    }

    public T andNe2(String column, Object value) {
        return and(column, Operator.LTGT, value);
    }

    public T andLlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.LLIKE, value.get());
        }
        return (T) this;
    }

    public T andLlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.LLIKE, value);
    }

    public T andLlike(String column, Object value) {
        return and(column, Operator.LLIKE, value);
    }

    public T andNotLlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.NOT_LLIKE, value.get());
        }
        return (T) this;
    }

    public T andNotLlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.NOT_LLIKE, value);
    }

    public T andNotLlike(String column, Object value) {
        return and(column, Operator.NOT_LLIKE, value);
    }

    public T andRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.RLIKE, value.get());
        }
        return (T) this;
    }

    public T andRlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.RLIKE, value);
    }

    public T andRlike(String column, Object value) {
        return and(column, Operator.RLIKE, value);
    }

    public T andNotRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.NOT_RLIKE, value.get());
        }
        return (T) this;
    }

    public T andNotRlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.NOT_RLIKE, value);
    }

    public T andNotRlike(String column, Object value) {
        return and(column, Operator.NOT_RLIKE, value);
    }

    public T andLRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.LRLIKE, value.get());
        }
        return (T) this;
    }

    public T andLRlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.LRLIKE, value);
    }

    public T andLRlike(String column, Object value) {
        return and(column, Operator.LRLIKE, value);
    }

    public T andNotLRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.NOT_LRLIKE, value.get());
        }
        return (T) this;
    }

    public T andNotLRlike(Boolean predicate, String column, Object value) {
        return and(predicate, column, Operator.NOT_LRLIKE, value);
    }

    public T andNotLRlike(String column, Object value) {
        return and(column, Operator.NOT_LRLIKE, value);
    }

    public T andIn(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.IN, values.get());
        }
        return (T) this;
    }

    public T andIn(Boolean predicate, String column, Object... values) {
        return and(predicate, column, Operator.IN, values);
    }

    public T andIn(String column, Object... values) {
        return and(column, Operator.IN, values);
    }

    public T andNotIn(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.NOT_IN, values.get());
        }
        return (T) this;
    }

    public T andNotIn(Boolean predicate, String column, Object... values) {
        return and(predicate, column, Operator.NOT_IN, values);
    }

    public T andNotIn(String column, Object... values) {
        return and(column, Operator.NOT_IN, values);
    }

    public T andBetween(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.BETWEEN_AND, values.get());
        }
        return (T) this;
    }

    public T andBetween(Boolean predicate, String column, Supplier<Object> value1, Supplier<Object> value2) {
        if (Boolean.TRUE.equals(predicate)) {
            return and(column, Operator.BETWEEN_AND, value1.get(), value2.get());
        }
        return (T) this;
    }

    public T andBetween(Boolean predicate, String column, Object... values) {
        return and(predicate, column, Operator.BETWEEN_AND, values);
    }

    public T andBetween(Boolean predicate, String column, Object value1, Object value2) {
        return and(predicate, column, Operator.BETWEEN_AND, value1, value2);
    }

    public T andBetween(String column, Object... values) {
        return and(column, Operator.BETWEEN_AND, values);
    }

    public T andBetween(String column, Object value1, Object value2) {
        return and(column, Operator.BETWEEN_AND, value1, value2);
    }

    public T andNull(Boolean predicate, String column) {
        return and(predicate, column, Operator.IS_NULL);
    }

    public T andNull(String column) {
        return and(column, Operator.IS_NULL);
    }

    public T andNotNull(Boolean predicate, String column) {
        return and(predicate, column, Operator.NOT_NULL);
    }

    public T andNotNull(String column) {
        return and(column, Operator.NOT_NULL);
    }



    public T orEq(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.EQ, value.get());
        }
        return (T) this;
    }

    public T orEq(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.EQ, value);
    }

    public T orEq(String column, Object value) {
        return or(column, Operator.EQ, value);
    }

    public T orGt(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.GT, value.get());
        }
        return (T) this;
    }

    public T orGt(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.GT, value);
    }

    public T orGt(String column, Object value) {
        return or(column, Operator.GT, value);
    }

    public T orGe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.GE, value.get());
        }
        return (T) this;
    }

    public T orGe(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.GE, value);
    }

    public T orGe(String column, Object value) {
        return or(column, Operator.GE, value);
    }

    public T orLt(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.LT, value.get());
        }
        return (T) this;
    }

    public T orLt(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.LT, value);
    }

    public T orLt(String column, Object value) {
        return or(column, Operator.LT, value);
    }

    public T orLe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.LE, value.get());
        }
        return (T) this;
    }

    public T orLe(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.LE, value);
    }

    public T orLe(String column, Object value) {
        return or(column, Operator.LE, value);
    }

    public T orNe(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.NE, value.get());
        }
        return (T) this;
    }

    public T orNe(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.NE, value);
    }

    public T orNe(String column, Object value) {
        return or(column, Operator.NE, value);
    }

    public T orNe2(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.LTGT, value.get());
        }
        return (T) this;
    }

    public T orNe2(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.LTGT, value);
    }

    public T orNe2(String column, Object value) {
        return or(column, Operator.LTGT, value);
    }

    public T orLlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.LLIKE, value.get());
        }
        return (T) this;
    }

    public T orLlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.LLIKE, value);
    }

    public T orLlike(String column, Object value) {
        return or(column, Operator.LLIKE, value);
    }

    public T orNotLlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.NOT_LLIKE, value.get());
        }
        return (T) this;
    }

    public T orNotLlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.NOT_LLIKE, value);
    }

    public T orNotLlike(String column, Object value) {
        return or(column, Operator.NOT_LLIKE, value);
    }

    public T orRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.RLIKE, value.get());
        }
        return (T) this;
    }

    public T orRlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.RLIKE, value);
    }

    public T orRlike(String column, Object value) {
        return or(column, Operator.RLIKE, value);
    }

    public T orNotRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.NOT_RLIKE, value.get());
        }
        return (T) this;
    }

    public T orNotRlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.NOT_RLIKE, value);
    }

    public T orNotRlike(String column, Object value) {
        return or(column, Operator.NOT_RLIKE, value);
    }

    public T orLRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.LRLIKE, value.get());
        }
        return (T) this;
    }

    public T orLRlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.LRLIKE, value);
    }

    public T orLRlike(String column, Object value) {
        return or(column, Operator.LRLIKE, value);
    }

    public T orNotLRlike(Boolean predicate, String column, Supplier<Object> value) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.NOT_LRLIKE, value.get());
        }
        return (T) this;
    }

    public T orNotLRlike(Boolean predicate, String column, Object value) {
        return or(predicate, column, Operator.NOT_LRLIKE, value);
    }

    public T orNotLRlike(String column, Object value) {
        return or(column, Operator.NOT_LRLIKE, value);
    }

    public T orIn(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.IN, values.get());
        }
        return (T) this;
    }

    public T orIn(Boolean predicate, String column, Object... values) {
        return or(predicate, column, Operator.IN, values);
    }

    public T orIn(String column, Object... values) {
        return or(column, Operator.IN, values);
    }

    public T orNotIn(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.NOT_IN, values.get());
        }
        return (T) this;
    }

    public T orNotIn(Boolean predicate, String column, Object... values) {
        return or(predicate, column, Operator.NOT_IN, values);
    }

    public T orNotIn(String column, Object... values) {
        return or(column, Operator.NOT_IN, values);
    }

    public T orBetween(Boolean predicate, String column, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.BETWEEN_AND, values.get());
        }
        return (T) this;
    }

    public T orBetween(Boolean predicate, String column, Supplier<Object> value1, Supplier<Object> value2) {
        if (Boolean.TRUE.equals(predicate)) {
            return or(column, Operator.BETWEEN_AND, value1.get(), value2.get());
        }
        return (T) this;
    }

    public T orBetween(Boolean predicate, String column, Object... values) {
        return or(predicate, column, Operator.BETWEEN_AND, values);
    }

    public T orBetween(Boolean predicate, String column, Object value1, Object value2) {
        return or(predicate, column, Operator.BETWEEN_AND, value1, value2);
    }

    public T orBetween(String column, Object... values) {
        return or(column, Operator.BETWEEN_AND, values);
    }

    public T orBetween(String column, Object value1, Object value2) {
        return or(column, Operator.BETWEEN_AND, value1, value2);
    }

    public T orNull(Boolean predicate, String column) {
        return or(predicate, column, Operator.IS_NULL);
    }

    public T orNull(String column) {
        return or(column, Operator.IS_NULL);
    }

    public T orNotNull(Boolean predicate, String column) {
        return or(predicate, column, Operator.NOT_NULL);
    }

    public T orNotNull(String column) {
        return or(column, Operator.NOT_NULL);
    }

    private T conditionQueryCriteria(Object ...queryCriteria) {
        T conditionSqlBuilder = null;
        for (Object criteria : queryCriteria) {
            Class<?> criteriaClass = criteria.getClass();
            List<Alias> fields = ObjectMapperUtils.getColumnFields(criteriaClass);
            for (Alias alias : fields) {
                try {
                    Field field = ObjectMapperUtils.getField(criteriaClass, alias.getAlias());
                    Object value = ObjectMapperUtils.getFieldValue(criteria, alias.getAlias());
                    if (field != null && value != null) {
                        Queries queriesAnnotation = field.getAnnotation(Queries.class);
                        Query queryAnnotation;
                        Query[] queries = null;
                        if (queriesAnnotation != null && queriesAnnotation.value().length > 0) {
                            queries = queriesAnnotation.value();
                        } else if ((queryAnnotation = field.getAnnotation(Query.class)) != null) {
                            queries = new Query[]{queryAnnotation};
                        }
                        if (queries != null) {
                            for (Query query : queries) {
                                if (query != null) {
                                    Object queryValue = handleQuery(query, alias, criteria, value);
                                    if (queryValue != null) {
                                        conditionSqlBuilder = handleQueryConditionBuilder(conditionSqlBuilder, query, alias, queryValue);
                                    }
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
                    log.warn("The conditional model " + criteria.getClass().getName() + " field " + alias.getAlias() + " handles an exception.");
                    throw new SQLBuilderException("The conditional model " + criteria.getClass().getName() + " field " + alias.getAlias() + " handles an exception.", e);
                }
            }
        }
        return conditionSqlBuilder;
    }

    private Object handleQuery(Query query, Alias alias, Object criteria, Object fieldValue) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object value = fieldValue;
        Class<?> mapFunClass = query.mapFun();
        String attr = query.attr();
        if (mapFunClass != Void.class) {
            Object mapFun = ObjectMapperUtils.getSingleObject(mapFunClass);
            if (mapFun instanceof Function) {
                value = ((Function) mapFun).apply(fieldValue);
            } else if (mapFun instanceof BiFunction) {
                value = ((BiFunction) mapFun).apply(criteria, fieldValue);
            } else {
                throw new SQLBuilderException("field \"" + alias.getAlias() + "\" mapFun \"" + mapFunClass.getName() +"\" does not implement the java.util.Function interface");
            }
        }
        else if (!"".equals(attr)) {
            value = ObjectMapperUtils.getAttr(fieldValue, attr.split("\\."), 0);
        }
        else if (query.openBooleanToConst()) {
            // Open "bool to const" mode to handle const value rather than bool value. Therefore, value should be reinitialized to null.
            value = null;
            if (query.trueToConstType() != Void.class && Boolean.TRUE.equals(fieldValue)) {
                value = CastUtils.strTo(query.trueToConst(), query.trueToConstType());
            } else if (query.falseToConstType() != Void.class && Boolean.FALSE.equals(fieldValue)) {
                value = CastUtils.strTo(query.falseToConst(), query.falseToConstType());
            }
        }
        return value;
    }

    private T handleQueryConditionBuilder(T conditionSqlBuilder, Query query, Alias alias, Object queryValue) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String columnName = "".equals(query.value()) ? alias.getOrigin() : query.value();
        if (conditionSqlBuilder == null) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(columnName, query.type(), queryValue);
            return (T) ObjectMapperUtils.getInstance(getClass(), Constants.CONDITION_CONSTRUCTOR_PARAMETER_TYPES,
                true, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return  (T) conditionSqlBuilder.and(columnName, query.type(), queryValue);
    }

    private void addCondition(String condition, int level) {
        if (conditionBuilder.length() > 0 && this.level < level) {
            conditionBuilder.insert(0, "(").append(")").append(condition);
        } else {
            conditionBuilder.append(condition);
        }
        this.level = level;
    }

    @Override
    public String build() {
        boolean prefixEmpty = prefix == null || "".equals(prefix), conditionEmpty = conditionBuilder.length() == 0;
        if (prefixEmpty && conditionEmpty) return "";
        if (prefixEmpty) return conditionBuilder.toString();
        if (conditionEmpty) return prefix;
        return ConditionUtils.parseTemplate(precompileSQL(), precompileArgs());
    }

    @Override
    public String precompileSQL() {
        return prefix + " " + sign + " " + conditionBuilder.toString();
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs.toArray(Constants.EMPTY_OBJECT_ARRAY);
    }
}
