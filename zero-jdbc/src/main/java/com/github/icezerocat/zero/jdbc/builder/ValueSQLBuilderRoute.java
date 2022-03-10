package com.github.icezerocat.zero.jdbc.builder;

/**
 * @author dragons
 * @date 2021/11/11 12:32
 */
public interface ValueSQLBuilderRoute extends SQLBuilder {

    default ValueSQLBuilder value() {
        return new ValueSQLBuilder(precompileSQL(), precompileArgs());
    }

}
