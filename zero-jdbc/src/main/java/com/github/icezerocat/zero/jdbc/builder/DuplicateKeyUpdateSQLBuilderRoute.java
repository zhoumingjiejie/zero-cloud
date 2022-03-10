package com.github.icezerocat.zero.jdbc.builder;

/**
 * @author dragons
 * @date 2021/12/7 17:12
 */
public interface DuplicateKeyUpdateSQLBuilderRoute extends SQLBuilder {

    default DuplicateKeyUpdateSQLBuilder onDuplicateKeyUpdateColumn(String column) {
        return new DuplicateKeyUpdateSQLBuilder(precompileSQL(), precompileArgs(), column + " = values(" + column + ")");
    }

    default DuplicateKeyUpdateSQLBuilder onDuplicateKeyUpdateSetter(String setter) {
        return new DuplicateKeyUpdateSQLBuilder(precompileSQL(), precompileArgs(), setter);
    }
}
