package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.entry.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragons
 * @date 2021/11/10 19:45
 */
public class UnionSQLBuilder implements SQLBuilder, OrderSQLBuilderRoute, LimitSQLBuilderRoute {

    private final String prefix;

    private final List<SQLBuilder> unions;

    private final List<Object> precompileArgs = new ArrayList<>();

    protected UnionSQLBuilder(String prefix, Object[] precompileArgs, SQLBuilder...unions) {
        this.prefix = prefix;
        this.unions = new ArrayList<>(Arrays.asList(unions));
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        for (SQLBuilder union : unions) {
            this.precompileArgs.addAll(Arrays.asList(union.precompileArgs()));
        }
    }

    public UnionSQLBuilder union(SQLBuilder...unions) {
        this.unions.addAll(Arrays.asList(unions));
        for (SQLBuilder union : unions) {
            this.precompileArgs.addAll(Arrays.asList(union.precompileArgs()));
        }
        return this;
    }

    @Override
    public String precompileSQL() {
        boolean prefixEmpty = prefix == null || "".equals(prefix), unionsEmpty = unions.isEmpty();
        if (prefixEmpty && unionsEmpty) return "";
        if (prefixEmpty) return unions.stream().map(PreparedStatementSupport::precompileSQL).collect(Collectors.joining(" UNION "));
        if (unionsEmpty) return prefix;
        return prefix + " UNION " + unions.stream().map(PreparedStatementSupport::precompileSQL).collect(Collectors.joining(" UNION "));
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs.toArray(Constants.EMPTY_OBJECT_ARRAY);
    }
}
