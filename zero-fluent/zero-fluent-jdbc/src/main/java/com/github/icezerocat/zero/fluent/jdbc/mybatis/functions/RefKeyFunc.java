package com.github.icezerocat.zero.fluent.jdbc.mybatis.functions;

import java.util.function.Function;

/**
 * IEntity实例关联键值构造器
 *
 * @param <E> IEntity类型
 * @author darui.wu
 */
@FunctionalInterface
public interface RefKeyFunc<E> extends Function<E, String> {
}
