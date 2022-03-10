package com.github.icezerocat.zero.jdbc.builder;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 19:34
 */
public class HavingSQLBuilder extends ConditionSQLBuilder<HavingSQLBuilder> implements SQLBuilder, OrderSQLBuilderRoute, LimitSQLBuilderRoute, UnionSQLBuilderRoute {


    protected HavingSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, HavingSQLBuilder b) {
        super(predicate, prefix, precompileArgs, b);
        sign = "HAVING";
    }

    protected HavingSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs) {
        super(predicate, prefix, precompileArgs);
        sign = "HAVING";
    }

    protected HavingSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Object... values) {
        super(predicate, prefix, precompileArgs, condition, values);
        sign = "HAVING";
    }

    protected HavingSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, String condition, Supplier<Object[]> supplier) {
        super(predicate, prefix, precompileArgs, condition, supplier);
        sign = "HAVING";
    }

    protected HavingSQLBuilder(Boolean predicate, String prefix, Object[] precompileArgs, Object... queryCriteria) {
        super(predicate, prefix, precompileArgs, queryCriteria);
        sign = "HAVING";
    }
}
