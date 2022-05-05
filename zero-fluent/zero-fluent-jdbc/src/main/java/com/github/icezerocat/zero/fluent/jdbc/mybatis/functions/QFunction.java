package com.github.icezerocat.zero.fluent.jdbc.mybatis.functions;

import java.util.function.Function;

/**
 * 入参和出参是同一个对象的Function
 *
 * @param <R>
 * @author wudarui
 */
@FunctionalInterface
public interface QFunction<R> extends Function<R, R> {
}
