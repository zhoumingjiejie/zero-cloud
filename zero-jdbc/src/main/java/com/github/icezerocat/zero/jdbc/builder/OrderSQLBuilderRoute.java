package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Order;

import java.util.function.Supplier;

/**
 * @author dragons
 * @date 2021/11/10 19:20
 */
public interface OrderSQLBuilderRoute extends SQLBuilder {

    default OrderSQLBuilder orderBy(String sort) {
        return orderBy(true, sort);
    }

    default OrderSQLBuilder orderBy(boolean predicate, String sort) {
        return orderBy(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, sort);
    }

    default OrderSQLBuilder orderBy(Supplier<Boolean> predicate, String sort) {
        return new OrderSQLBuilder(predicate, precompileSQL(), precompileArgs(), sort);
    }

    default OrderSQLBuilder orderBy(String column, Order order) {
        return orderBy(Constants.TRUE_PREDICATE, column, order);
    }

    default OrderSQLBuilder orderBy(boolean predicate, String column, Order order) {
        return orderBy(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, column, order);
    }

    default OrderSQLBuilder orderBy(Supplier<Boolean> predicate, String column, Order order) {
        return new OrderSQLBuilder(predicate, precompileSQL(), precompileArgs(), column, order);
    }

    default OrderSQLBuilder orderByAsc(String... columns) {
        return orderByAsc(Constants.TRUE_PREDICATE, columns);
    }

    default OrderSQLBuilder orderByAsc(boolean predicate, String... columns) {
        return orderByAsc(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, columns);
    }

    default OrderSQLBuilder orderByAsc(Supplier<Boolean> predicate, String... columns) {
        if (columns.length == 0) throw new SQLBuilderException("Column cannot be null in order by sql.");
        OrderSQLBuilder builder = new OrderSQLBuilder(predicate, precompileSQL(), precompileArgs(), columns[0], Order.ASC);
        for (int i = 1; i < columns.length; i++) {
            builder.addAsc(predicate, columns[i]);
        }
        return builder;
    }


    default OrderSQLBuilder orderByDesc(String... columns) {
        return orderByDesc(Constants.TRUE_PREDICATE, columns);
    }

    default OrderSQLBuilder orderByDesc(boolean predicate, String... columns) {
        return orderByDesc(predicate ? Constants.TRUE_PREDICATE : Constants.FALSE_PREDICATE, columns);
    }

    default OrderSQLBuilder orderByDesc(Supplier<Boolean> predicate, String... columns) {
        if (columns.length == 0) throw new SQLBuilderException("Column cannot be null in order by sql.");
        OrderSQLBuilder builder = new OrderSQLBuilder(predicate, precompileSQL(), precompileArgs(), columns[0], Order.DESC);
        for (int i = 1; i < columns.length; i++) {
            builder.addAsc(predicate, columns[i]);
        }
        return builder;
    }
}
