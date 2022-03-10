package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dragons
 * @date 2021/11/11 13:30
 */
public class SetSQLBuilder implements SQLBuilder, FromSQLBuilderRoute, WhereSQLBuilderRoute {

    private final String prefix;

    private final List<String> setters;

    private final List<Object> precompileArgs = new ArrayList<>();

    protected SetSQLBuilder(String prefix, Object[] precompileArgs) {
        this.prefix = prefix;
        this.setters = new ArrayList<>();
        this.precompileArgs.addAll(Arrays.asList(precompileArgs));
    }

    protected SetSQLBuilder(String prefix, Object[] precompileArgs, String column, Object value) {
        this(prefix, precompileArgs);
        addSet(column, value);
    }

    protected SetSQLBuilder(String prefix, Object[] precompileArgs, String setter) {
        this(prefix, precompileArgs);
        setters.add(setter);
    }

    public SetSQLBuilder addSet(String setter, Object...values) {
        setters.add(setter);
        if (values.length > 0) {
            precompileArgs.addAll(Arrays.asList(values));
        }
        return this;
    }

    public SetSQLBuilder addSet(String column, Object value) {
        Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, Operator.EQ, value);
        return addSet(pt._1, pt._2);
    }

    public SetSQLBuilder addSetColumn(String column1, String column2) {
        return addSet(ConditionUtils.parseConditionColumn(column1, Operator.EQ, column2));
    }

    @Override
    public String precompileSQL() {
        boolean prefixEmpty = prefix == null || "".equals(prefix), settersEmpty = setters.isEmpty();
        if (prefixEmpty && settersEmpty) return "";
        if (settersEmpty) return prefix;
        if (prefixEmpty) return String.join(", ", setters);
        return prefix + " SET " + String.join(", ", setters);
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs.toArray(Constants.EMPTY_OBJECT_ARRAY);
    }
}
