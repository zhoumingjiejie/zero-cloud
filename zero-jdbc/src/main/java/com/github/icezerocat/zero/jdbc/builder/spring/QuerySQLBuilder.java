package com.github.icezerocat.zero.jdbc.builder.spring;

import com.github.icezerocat.zero.jdbc.builder.*;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;

/**
 * @author dragons
 * @date 2021/12/22 18:56
 */
public class QuerySQLBuilder implements SQLBuilder, WhereSQLBuilderRoute, JoinSQLBuilderRoute, GroupSQLBuilderRoute, OrderSQLBuilderRoute, LimitSQLBuilderRoute {

    public final static QuerySQLBuilder INSTANCE = new QuerySQLBuilder();

    public QuerySQLBuilder() {
    }

    @Override
    public String precompileSQL() {
        return " ";
    }

    @Override
    public Object[] precompileArgs() {
        return Constants.EMPTY_OBJECT_ARRAY;
    }
}
