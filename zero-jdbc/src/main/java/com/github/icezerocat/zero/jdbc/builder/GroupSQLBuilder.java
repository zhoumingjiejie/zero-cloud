package com.github.icezerocat.zero.jdbc.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragons
 * @date 2021/11/10 18:22
 */
public class GroupSQLBuilder implements SQLBuilder, HavingSQLBuilderRoute, OrderSQLBuilderRoute, LimitSQLBuilderRoute, UnionSQLBuilderRoute {

    private final String prefix;

    private final List<String> columns;

    private final Object[] precompileArgs;

    protected GroupSQLBuilder(String prefix, Object[] precompileArgs, String ...columns) {
        this.prefix = prefix;
        this.columns = new ArrayList<>(Arrays.asList(columns));
        this.precompileArgs = precompileArgs;
    }

    protected GroupSQLBuilder(String prefix, Object[] precompileArgs, SQLBuilder...columns) {
        this(prefix, precompileArgs, Arrays.stream(columns).map(s -> "(" + s.precompileSQL() + ")").toArray(String[]::new));
    }

    public GroupSQLBuilder addColumn(String ...columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public GroupSQLBuilder addColumn(SQLBuilder...columns) {
        this.columns.addAll(Arrays.stream(columns).map(s -> "(" + s.build() + ")").collect(Collectors.toList()));
        return this;
    }

    @Override
    public String precompileSQL() {
        boolean prefixEmpty = prefix == null || "".equals(prefix), columnsEmpty = columns.isEmpty();
        if (prefixEmpty && columnsEmpty) return "";
        if (prefixEmpty) return String.join(", ", columns);
        if (columnsEmpty) return prefix;
        return prefix + " GROUP BY " + String.join(", ", columns);
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs;
    }
}
