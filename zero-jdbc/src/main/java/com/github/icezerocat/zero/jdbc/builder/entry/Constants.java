package com.github.icezerocat.zero.jdbc.builder.entry;

import java.util.function.Supplier;

/**
 * 常量
 *
 * @author zero
 * @date 2021/11/10 17:00
 */
public interface Constants {
    Supplier<Boolean> TRUE_PREDICATE = () -> true;

    Supplier<Boolean> FALSE_PREDICATE = () -> false;

    Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    Class[] CONDITION_CONSTRUCTOR_PARAMETER_TYPES = new Class[]{Boolean.class, String.class, Object[].class, String.class, Object[].class};
}
