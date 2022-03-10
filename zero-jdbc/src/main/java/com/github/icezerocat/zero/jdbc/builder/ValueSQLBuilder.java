package com.github.icezerocat.zero.jdbc.builder;


/**
 * @author dragons
 * @date 2021/11/11 12:00
 */
public class ValueSQLBuilder extends ValuesSQLBuilder {

    protected ValueSQLBuilder(String prefix, Object[] precompileArgs) {
        super(prefix, precompileArgs);
        sign = "VALUE";
    }

}
