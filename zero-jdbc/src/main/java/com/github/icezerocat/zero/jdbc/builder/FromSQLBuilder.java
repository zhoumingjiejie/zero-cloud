package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dragons
 * @date 2021/11/9 18:30
 */
public class FromSQLBuilder implements SQLBuilder, WhereSQLBuilderRoute, JoinSQLBuilderRoute, LimitSQLBuilderRoute,
        OrderSQLBuilderRoute, UnionSQLBuilderRoute, GroupSQLBuilderRoute {

    private final static Logger log = LoggerFactory.getLogger(FromSQLBuilder.class);

    private final String prefix;

    private final List<String> tables;

    private final Object[] precompileArgs;

    protected FromSQLBuilder(String prefix, Object[] precompileArgs, String... tables) {
        this.prefix = prefix;
        this.tables = new ArrayList<>(Arrays.asList(tables));
        this.precompileArgs = precompileArgs;
    }

    protected FromSQLBuilder(String prefix, Object[] precompileArgs, Alias... tables) {
        this.prefix = prefix;
        this.tables = new ArrayList<>();
        this.precompileArgs = precompileArgs;
        this.addTable(tables);
    }

    protected FromSQLBuilder(String prefix, Object[] precompileArgs, Object... tables) {
        this.prefix = prefix;
        this.tables = new ArrayList<>();
        this.precompileArgs = precompileArgs;
        Arrays.stream(tables).forEach(e -> {
            if (e instanceof CharSequence) {
                this.tables.add(e.toString());
            } else if (e instanceof Alias) {
                addTable((Alias) e);
            } else if (e instanceof Class) {
                this.tables.add(ObjectMapperUtils.getTableName((Class<?>) e));
            } else {
                log.warn("Table type " + e.getClass().getName() + " is an unrecognized type in from sql, ignore.");
            }
        });
    }

    /**
     * 添加表单
     *
     * @param tables 表单名
     * @return builder
     */
    public FromSQLBuilder addTable(String... tables) {
        this.tables.addAll(Arrays.asList(tables));
        return this;
    }

    /**
     * 添加表单
     *
     * @param tables 表单别名
     * @return builder
     */
    public FromSQLBuilder addTable(Alias... tables) {
        this.tables.addAll(Arrays.stream(tables).map(Alias::toFormString).collect(Collectors.toList()));
        return this;
    }

    /**
     * 预编译sql
     *
     * @return sql
     */
    @Override
    public String precompileSQL() {
        if (tables.size() == 0) {
            return prefix;
        }
        return prefix + " FROM " + String.join(", ", tables);
    }

    /**
     * 预编译参数
     *
     * @return 参数
     */
    @Override
    public Object[] precompileArgs() {
        return precompileArgs;
    }
}
