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
 * @date 2021/11/9 18:28
 */
public class SelectSQLBuilder implements SQLBuilder, FromSQLBuilderRoute, OrderSQLBuilderRoute, LimitSQLBuilderRoute, UnionSQLBuilderRoute {

    private final static Logger log = LoggerFactory.getLogger(SelectSQLBuilder.class);

    private final String prefix;

    private final Object[] precompileArgs;

    private final List<String> columns;

    protected SelectSQLBuilder(String prefix, Object[] precompileArgs, String... columns) {
        this.prefix = prefix;
        this.precompileArgs = precompileArgs;
        this.columns = new ArrayList<>(Arrays.asList(columns));
    }

    protected SelectSQLBuilder(String prefix, Object[] precompileArgs, Alias... columns) {
        this.prefix = prefix;
        this.precompileArgs = precompileArgs;
        this.columns = new ArrayList<>(columns.length);
        addColumn(columns);
    }

    protected SelectSQLBuilder(String prefix, Object[] precompileArgs, Object... columns) {
        this.prefix = prefix;
        this.precompileArgs = precompileArgs;
        this.columns = new ArrayList<>(columns.length);
        Arrays.stream(columns).forEach(e -> {
            if (e instanceof CharSequence) {
                this.columns.add(e.toString());
            } else if (e instanceof Alias) {
                this.addColumn((Alias) e);
            } else if (e instanceof Class) {
                this.columns.addAll(ObjectMapperUtils.getColumns((Class<?>) e));
            } else {
                log.warn("Column type " + e.getClass().getName() + " is an unrecognized type in from sql, ignore.");
            }
        });
    }


    /**
     * 添加select列
     *
     * @param columns 列
     * @return builder
     */
    public SelectSQLBuilder addColumn(String... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 添加select列和别名
     *
     * @param columns 列和别名
     * @return builder
     */
    public SelectSQLBuilder addColumn(Alias... columns) {
        this.columns.addAll(Arrays.stream(columns).map(Alias::toString).collect(Collectors.toList()));
        return this;
    }

    /**
     * 预编译sql
     *
     * @return sql
     */
    @Override
    public String precompileSQL() {
        if (prefix == null || "".equals(prefix)) {
            return "SELECT " + String.join(", ", columns);
        }
        return prefix + " SELECT " + String.join(", ", columns);
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
