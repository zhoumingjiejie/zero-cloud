package com.github.icezerocat.zero.jdbc.builder;

/**
 * @author dragons
 * @date 2021/11/11 12:34
 */
public interface ValuesSQLBuilderRoute extends SQLBuilder {

    default ValuesSQLBuilder values() {
        return new ValuesSQLBuilder(precompileSQL(), precompileArgs());
    }
}
