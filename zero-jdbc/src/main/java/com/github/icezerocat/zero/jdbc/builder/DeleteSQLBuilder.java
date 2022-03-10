package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;

/**
 * @author dragons
 * @date 2021/11/11 20:19
 */
public class DeleteSQLBuilder implements SQLBuilder, WhereSQLBuilderRoute, FromSQLBuilderRoute {

    private final String table;

    protected DeleteSQLBuilder(String table) {
        this.table = table;
    }

    @Override
    public String precompileSQL() {
        return "DELETE FROM " + table;
    }

    @Override
    public Object[] precompileArgs() {
        return Constants.EMPTY_OBJECT_ARRAY;
    }
}
