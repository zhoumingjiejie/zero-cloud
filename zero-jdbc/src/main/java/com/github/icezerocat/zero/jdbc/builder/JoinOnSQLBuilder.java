package com.github.icezerocat.zero.jdbc.builder;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 16:42
 */
public class JoinOnSQLBuilder extends ConditionSQLBuilder<JoinOnSQLBuilder> implements SQLBuilder, WhereSQLBuilderRoute, LimitSQLBuilderRoute, OrderSQLBuilderRoute, UnionSQLBuilderRoute, GroupSQLBuilderRoute, JoinSQLBuilderRoute {


    protected JoinOnSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, JoinOnSQLBuilder b) {
        super(predicate, prefix, precompileArgs, b);
        sign = "ON";
    }

    protected JoinOnSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs) {
        super(predicate, prefix, precompileArgs);
        sign = "ON";
    }

    protected JoinOnSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Object... values) {
        super(predicate, prefix, precompileArgs, condition, values);
        sign = "ON";
    }

    protected JoinOnSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Supplier<Object[]> supplier) {
        super(predicate, prefix, precompileArgs, condition, supplier);
        sign = "ON";
    }

    protected JoinOnSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, Object... queryCriteria) {
        super(predicate, prefix, precompileArgs, queryCriteria);
        sign = "ON";
    }
}
