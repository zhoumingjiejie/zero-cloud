package com.github.icezerocat.zero.jdbc.builder;


/**
 * @author dragons
 * @date 2021/11/10 19:52
 */
public interface UnionSQLBuilderRoute extends SQLBuilder {

    default UnionSQLBuilder union(SQLBuilder... unions) {
        return new UnionSQLBuilder(precompileSQL(), precompileArgs(), unions);
    }
}
