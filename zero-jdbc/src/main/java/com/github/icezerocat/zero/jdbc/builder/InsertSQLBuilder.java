package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.InsertMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dragons
 * @date 2021/11/11 11:27
 */
public class InsertSQLBuilder implements SQLBuilder, ValueSQLBuilderRoute, ValuesSQLBuilderRoute, SelectSQLBuilderRoute {

    private final String table;

    private final InsertMode mode;

    private final List<String> columns;

    protected InsertSQLBuilder(String table, InsertMode mode, String ...columns) {
        this.table = table;
        this.mode = mode;
        this.columns = new ArrayList<>(Arrays.asList(columns));
    }

    public InsertSQLBuilder addColumn(String ...columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    @Override
    public String precompileSQL() {
        if (columns.isEmpty()) return mode.getPrefix() + " " + table;
        return mode.getPrefix() + " " + table + "(" + String.join(", ", columns)  + ")";
    }

    @Override
    public Object[] precompileArgs() {
        return Constants.EMPTY_OBJECT_ARRAY;
    }
}
