package com.github.icezerocat.zero.jdbc.builder;


import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 10:40
 */
public class WhereSQLBuilder extends ConditionSQLBuilder<WhereSQLBuilder> implements SQLBuilder, GroupSQLBuilderRoute, OrderSQLBuilderRoute, LimitSQLBuilderRoute, UnionSQLBuilderRoute {


    protected WhereSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, WhereSQLBuilder b) {
        super(predicate, prefix, precompileArgs, b);
    }

    protected WhereSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs) {
        super(predicate, prefix, precompileArgs);
    }

    protected WhereSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Object... values) {
        super(predicate, prefix, precompileArgs, condition, values);
    }

    protected WhereSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Supplier<Object[]> supplier) {
        super(predicate, prefix, precompileArgs, condition, supplier);
    }

    protected WhereSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, Object... queryCriteria) {
        super(predicate, prefix, precompileArgs, queryCriteria);
    }
}
