package com.github.icezerocat.zero.jdbc.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragons
 * @date 2021/12/7 16:57
 */
public class DuplicateKeyUpdateSQLBuilder implements SQLBuilder {

    private final String sign;

    private final String prefix;

    private final List<String> setters;

    private final Object[] precompileArgs;

    protected DuplicateKeyUpdateSQLBuilder(String prefix, Object[] precompileArgs) {
        this.sign = "values";
        this.prefix = prefix;
        this.setters = new ArrayList<>();
        this.precompileArgs = precompileArgs;
    }

    protected DuplicateKeyUpdateSQLBuilder(String prefix, Object[] precompileArgs, String setter) {
        this(prefix, precompileArgs);
        setters.add(setter);
    }

    public DuplicateKeyUpdateSQLBuilder addUpdateColumn(String column) {
        setters.add(column + " = " + sign + "(" + column + ")");
        return this;
    }

    public DuplicateKeyUpdateSQLBuilder addUpdateSetter(String setter) {
        setters.add(setter);
        return this;
    }

    @Override
    public String precompileSQL() {
        return prefix + " ON DUPLICATE KEY UPDATE " + setters.stream().collect(Collectors.joining(", "));
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs;
    }
}
