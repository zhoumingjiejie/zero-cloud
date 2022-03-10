package com.github.icezerocat.zero.jdbc.builder;


import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/9 20:05
 */
public class OrderSQLBuilder implements SQLBuilder, LimitSQLBuilderRoute, UnionSQLBuilderRoute {

    private final String prefix;

    private final List<String> sorts;

    private boolean passPredicate;

    private Object[] precompileArgs;

    protected OrderSQLBuilder(String prefix, Object[] precompileArgs) {
        this.prefix = prefix;
        this.sorts = new ArrayList<>();
        this.passPredicate = true;
        this.precompileArgs = precompileArgs;
    }

    protected OrderSQLBuilder(Supplier<Boolean> predicate, String prefix, Object[] precompileArgs, String sort) {
        this(prefix, precompileArgs);
        addSort(predicate, sort);
    }

    protected OrderSQLBuilder(Supplier<Boolean> predicate, String prefix, Object[] precompileArgs, String column, Order order) {
        this(prefix, precompileArgs);
        addSort(predicate, column, order);
    }

    public OrderSQLBuilder addAsc(String ...columns) {
        return addAsc(Constants.TRUE_PREDICATE, columns);
    }

    public OrderSQLBuilder addAsc(boolean predicate, String ...columns) {
        return addAsc(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, columns);
    }

    public OrderSQLBuilder addAsc(Supplier<Boolean> predicate, String ...columns) {
        if (prefix == null) passPredicate &= predicate.get();
        if (predicate.get() && passPredicate && columns.length > 0) {
            for (String column : columns) {
                addSort(column, Order.ASC);
            }
        }
        return this;
    }

    public OrderSQLBuilder addDesc(String ...columns) {
        return addDesc(Constants.TRUE_PREDICATE, columns);
    }

    public OrderSQLBuilder addDesc(boolean predicate, String ...columns) {
        return addDesc(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, columns);
    }

    public OrderSQLBuilder addDesc(Supplier<Boolean> predicate, String ...columns) {
        if (prefix == null) passPredicate &= predicate.get();
        if (predicate.get() && passPredicate && columns.length > 0) {
            for (String column : columns) {
                addSort(column, Order.DESC);
            }
        }
        return this;
    }

    public OrderSQLBuilder addSort(String column, Order order) {
        return addSort(Constants.TRUE_PREDICATE, column, order);
    }

    public OrderSQLBuilder addSort(boolean predicate, String column, Order order) {
        return addSort(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, column, order);
    }

    public OrderSQLBuilder addSort(Supplier<Boolean> predicate, String column, Order order) {
        if (prefix == null) passPredicate &= predicate.get();
        if (predicate.get() && passPredicate) {
            sorts.add(column + " " + order.toString());
        }
        return this;
    }

    public OrderSQLBuilder addSort(String sort) {
        return addSort(true, sort);
    }

    public OrderSQLBuilder addSort(boolean predicate, String sort) {
        return addSort(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, sort);
    }

    public OrderSQLBuilder addSort(Supplier<Boolean> predicate, String sort) {
        if (prefix == null) passPredicate &= predicate.get();
        if (predicate.get() && passPredicate) {
            sorts.add(sort);
        }
        return this;
    }

    public OrderSQLBuilder addSort(OrderSQLBuilder wrapper) {
        return addSort(Constants.TRUE_PREDICATE, wrapper);
    }

    public OrderSQLBuilder addSort(boolean predicate, OrderSQLBuilder wrapper) {
        return addSort(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, wrapper);
    }

    public OrderSQLBuilder addSort(Supplier<Boolean> predicate, OrderSQLBuilder wrapper) {
        if (prefix == null) passPredicate &= predicate.get();
        if (predicate.get() && passPredicate && !wrapper.sorts.isEmpty()) {
            this.sorts.addAll(wrapper.sorts);
        }
        return this;
    }

    @Override
    public String precompileSQL() {
        if (prefix == null || "".equals(prefix)) return String.join(", ", sorts);
        if (sorts.isEmpty()) return prefix;
        return prefix + " ORDER BY " + String.join(", ", sorts);
    }

    @Override
    public Object[] precompileArgs() {
        return precompileArgs;
    }
}
