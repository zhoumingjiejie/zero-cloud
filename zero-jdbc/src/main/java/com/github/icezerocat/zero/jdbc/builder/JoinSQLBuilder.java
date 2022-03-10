package com.github.icezerocat.zero.jdbc.builder;

/**
 * @author dragons
 * @date 2021/11/10 10:11
 */
public class JoinSQLBuilder implements SQLBuilder, JoinOnSQLBuilderRoute {

    private final String prefix;

    private final String joinType;

    private final String table;

    private final Object[] precompileArgs;

    protected JoinSQLBuilder(String prefix, Object[] precompileArgs, String joinType, String table) {
        this.prefix = prefix;
        this.precompileArgs = precompileArgs;
        this.joinType = joinType;
        this.table = table;
    }

    @Override
    public String precompileSQL() {
        return prefix + " " + joinType + " " + table;
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs;
    }
}
