package com.github.icezerocat.zero.jdbc.builder;


/**
 * @author dragons
 * @date 2021/11/10 19:14
 */
public interface LimitSQLBuilderRoute extends SQLBuilder {

    default LimitSQLBuilder limit(int count) {
        return new LimitSQLBuilder(precompileSQL(), precompileArgs(), count);
    }

    default LimitSQLBuilder limit(int offset, int count) {
        return new LimitSQLBuilder(precompileSQL(), precompileArgs(), offset, count);
    }

    default LimitSQLBuilder limit(Boolean predicate, int count) {
        if (Boolean.TRUE.equals(predicate)) {
            return new LimitSQLBuilder(precompileSQL(), precompileArgs(), count);
        }
        return new LimitSQLBuilder(precompileSQL(), precompileArgs());
    }

    default LimitSQLBuilder limit(Boolean predicate, int offset, int count) {
        if (Boolean.TRUE.equals(predicate)) {
            return new LimitSQLBuilder(precompileSQL(), precompileArgs(), offset, count);
        }
        return new LimitSQLBuilder(precompileSQL(), precompileArgs());
    }
}
