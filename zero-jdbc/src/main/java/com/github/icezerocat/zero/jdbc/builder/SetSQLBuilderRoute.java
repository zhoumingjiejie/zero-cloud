package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

/**
 * @author dragons
 * @date 2021/11/11 20:13
 */
public interface SetSQLBuilderRoute extends SQLBuilder {

    default SetSQLBuilder set(String setter) {
        return new SetSQLBuilder(precompileSQL(), precompileArgs(), setter);
    }

    default SetSQLBuilder set(String column, Object value) {
        return new SetSQLBuilder(precompileSQL(), precompileArgs(), column, value);
    }

    default SetSQLBuilder setColumn(String column1, String column2) {
        return new SetSQLBuilder(precompileSQL(), precompileArgs(), ConditionUtils.parseConditionColumn(column1, Operator.EQ, column2));
    }
}
