package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Alias;

/**
 * @author dragons
 * @date 2021/11/11 11:37
 */
public interface SelectSQLBuilderRoute extends SQLBuilder {

    default SelectSQLBuilder selectAll() {
        return new SelectSQLBuilder(precompileSQL(), precompileArgs(), "*");
    }

    default SelectSQLBuilder select(String... columns) {
        return new SelectSQLBuilder(precompileSQL(), precompileArgs(), columns);
    }

    default SelectSQLBuilder select(Alias... columns) {
        return new SelectSQLBuilder(precompileSQL(), precompileArgs(), columns);
    }

    default SelectSQLBuilder select(Object... columns) {
        return new SelectSQLBuilder(precompileSQL(), precompileArgs(), columns);
    }
}
