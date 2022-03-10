package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 19:36
 */
public interface HavingSQLBuilderRoute extends SQLBuilder {

    default HavingSQLBuilder having(Object... queryCriteria) {
        return having(Boolean.TRUE, queryCriteria);
    }

    default HavingSQLBuilder having(Boolean predicate, Object... queryCriteria) {
        return new HavingSQLBuilder(predicate, precompileSQL(), precompileArgs(), queryCriteria);
    }



    default HavingSQLBuilder having(String condition, Object... params) {
        return having(Boolean.TRUE, condition, params);
    }

    default HavingSQLBuilder having(Boolean predicate, String condition, Object... params) {
        return new HavingSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }

    default HavingSQLBuilder having(Boolean predicate, String condition, Supplier<Object[]> params) {
        return new HavingSQLBuilder(predicate, precompileSQL(), precompileArgs(), condition, params);
    }



    default HavingSQLBuilder having(String column, Operator option, Object... values) {
        return having(Boolean.TRUE, column, option, values);
    }

    default HavingSQLBuilder having(Boolean predicate, String column, Operator option, Object... values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values);
            return new HavingSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new HavingSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }

    default HavingSQLBuilder having(Boolean predicate, String column, Operator option, Supplier<Object[]> values) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, values.get());
            return new HavingSQLBuilder(Boolean.TRUE, precompileSQL(), precompileArgs(), pt._1, pt._2);
        }
        return new HavingSQLBuilder(Boolean.FALSE, precompileSQL(), precompileArgs());
    }
}
