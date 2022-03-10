package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;

/**
 * @author dragons
 * @date 2021/11/10 19:23
 */
public interface JoinSQLBuilderRoute extends SQLBuilder {
    default JoinSQLBuilder join(String table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "JOIN", table);
    }

    default JoinSQLBuilder join(Alias table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "JOIN", table.toString());
    }

    default JoinSQLBuilder join(Class aClass) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "JOIN", ObjectMapperUtils.getTableName(aClass));
    }

    default JoinSQLBuilder leftJoin(String table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "LEFT JOIN", table);
    }

    default JoinSQLBuilder leftJoin(Alias table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "LEFT JOIN", table.toString());
    }

    default JoinSQLBuilder leftJoin(Class aClass) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "LEFT JOIN", ObjectMapperUtils.getTableName(aClass));
    }

    default JoinSQLBuilder rightJoin(String table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "RIGHT JOIN", table);
    }

    default JoinSQLBuilder rightJoin(Alias table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "RIGHT JOIN", table.toString());
    }

    default JoinSQLBuilder rightJoin(Class aClass) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "RIGHT JOIN", ObjectMapperUtils.getTableName(aClass));
    }

    default JoinSQLBuilder fullJoin(String table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "FULL JOIN", table);
    }

    default JoinSQLBuilder fullJoin(Alias table) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "FULL JOIN", table.toString());
    }

    default JoinSQLBuilder fullJoin(Class aClass) {
        return new JoinSQLBuilder(precompileSQL(), precompileArgs(), "FULL JOIN", ObjectMapperUtils.getTableName(aClass));
    }
}
