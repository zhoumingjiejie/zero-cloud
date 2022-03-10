package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 19:22
 */
public interface WhereSQLBuilderRoute extends SQLBuilder {

    default WhereSQLBuilder where(WhereSQLBuilder whereSqlBuilder) {
        return where(Boolean.TRUE, whereSqlBuilder);
    }

    default WhereSQLBuilder where(Boolean predicate, WhereSQLBuilder whereSqlBuilder) {
        if (predicate) {
            return new WhereSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), whereSqlBuilder);
        }
        return new WhereSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }

    default WhereSQLBuilder where(Object... queryCriteria) {
        return where(Boolean.TRUE, queryCriteria);
    }

    default WhereSQLBuilder where(Boolean predicate, Object... queryCriteria) {
        return new WhereSQLBuilder(predicate, precompileSQL(), precompileArgs(), queryCriteria);
    }


    default WhereSQLBuilder where(String condition, Object... params) {
        return where(Boolean.TRUE, condition, params);
    }

    default WhereSQLBuilder where(Boolean predicate, String condition, Object... params) {
        return new WhereSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }

    default WhereSQLBuilder where(Boolean predicate, String condition, Supplier<Object[]> params) {
        return new WhereSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }



    default WhereSQLBuilder where(String column, Operator option, Object... values) {
        return where(Boolean.TRUE, column, option, values);
    }

    default WhereSQLBuilder where(Boolean predicate, String column, Operator option, Object... values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
            return new WhereSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new WhereSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }

    default WhereSQLBuilder where(Boolean predicate, String column, Operator option, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
            return new WhereSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new WhereSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }



}
