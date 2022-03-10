package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;

/**
 * @author dragons
 * @date 2021/11/11 13:08
 */
public class UpdateSQLBuilder implements SQLBuilder, SetSQLBuilderRoute {

    private final String table;

    protected UpdateSQLBuilder(String table) {
        this.table = table;
    }

    @Override
    public String precompileSQL() {
        return "UPDATE " + table;
    }

    @Override
    public Object[] precompileArgs() {
        return Constants.EMPTY_OBJECT_ARRAY;
    }
}
