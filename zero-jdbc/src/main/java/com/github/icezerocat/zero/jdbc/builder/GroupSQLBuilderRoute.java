package com.github.icezerocat.zero.jdbc.builder;


/**
 * @author dragons
 * @date 2021/11/10 19:28
 */
public interface GroupSQLBuilderRoute extends SQLBuilder {

    default GroupSQLBuilder groupBy(String... columns) {
        return new GroupSQLBuilder(precompileSQL(), precompileArgs(), columns);
    }

    default GroupSQLBuilder groupBy(SQLBuilder... subQueries) {
        return new GroupSQLBuilder(precompileSQL(), precompileArgs(), subQueries);
    }
}
