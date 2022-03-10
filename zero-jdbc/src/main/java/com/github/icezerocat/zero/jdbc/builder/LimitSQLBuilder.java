package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.entry.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dragons
 * @date 2021/11/9 20:14
 */
public class LimitSQLBuilder implements SQLBuilder, UnionSQLBuilderRoute {

    private final String prefix;

    private final String limitSql;

    private final List<Object> precompileArgs = new ArrayList<>();

    protected LimitSQLBuilder(String prefix, Object[] precompileArgs) {
        this.prefix = prefix;
        this.limitSql = null;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
    }

    protected LimitSQLBuilder(String prefix, Object[] precompileArgs, int count) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        this.precompileArgs.add(count);
        limitSql = "?";
    }

    protected LimitSQLBuilder(String prefix, Object[] precompileArgs, int offset, int count) {
        this.prefix = prefix;
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
        this.precompileArgs.add(offset);
        this.precompileArgs.add(count);
        limitSql = "?, ?";
    }

    @Override
    public String precompileSQL() {
        if (prefix == null || "".equals(prefix)) return limitSql;
        if (limitSql == null || "".equals(limitSql)) return prefix;
        return prefix + " LIMIT " + limitSql;
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs.toArray(Constants.EMPTY_OBJECT_ARRAY);
    }
}
