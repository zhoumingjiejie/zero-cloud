package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 19:24
 */
public interface JoinOnSQLBuilderRoute extends SQLBuilder {

    default JoinOnSQLBuilder on(Object... queryCriteria) {
        return on(Boolean.TRUE, queryCriteria);
    }

    default JoinOnSQLBuilder on(Boolean predicate, Object... queryCriteria) {
        return new JoinOnSQLBuilder(predicate, precompileSQL(), precompileArgs(), queryCriteria);
    }

    default JoinOnSQLBuilder on(String condition, Object... params) {
        return on(Boolean.TRUE, condition, params);
    }

    default JoinOnSQLBuilder on(Boolean predicate, String condition, Object... params) {
        return new JoinOnSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }

    default JoinOnSQLBuilder on(Boolean predicate, String condition, Supplier<Object[]> params) {
        return new JoinOnSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }


    default JoinOnSQLBuilder on(String column, Operator option, Object... values) {
        return on(Boolean.TRUE, column, option, values);
    }

    default JoinOnSQLBuilder on(Boolean predicate, String column, Operator option, Object... values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
            return new JoinOnSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new JoinOnSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs());
    }

    default JoinOnSQLBuilder on(Boolean predicate, String column, Operator option, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
            return new JoinOnSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new JoinOnSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }

}
