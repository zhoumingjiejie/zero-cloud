package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Alias;

/**
 * @author dragons
 * @date 2021/11/10 19:26
 */
public interface FromSQLBuilderRoute extends SQLBuilder {

    default FromSQLBuilder from(String... tableNames) {
        return new FromSQLBuilder(precompileSQL(), precompileArgs(), tableNames);
    }

    default FromSQLBuilder from(Alias... tableNames) {
        return new FromSQLBuilder(precompileSQL(), precompileArgs(), tableNames);
    }

    default FromSQLBuilder from(Object... tableNames) {
        return new FromSQLBuilder(precompileSQL(), precompileArgs(), tableNames);
    }
}
