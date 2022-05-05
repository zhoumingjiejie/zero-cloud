package com.github.icezerocat.zero.fluent.jdbc.mybatis.functions;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.metadata.FieldMeta;

import java.util.function.Predicate;

/**
 * FieldPredicate: 对判断字段是否符合条件
 *
 * @author wudarui
 */
@FunctionalInterface
public interface FieldPredicate extends Predicate<FieldMeta> {
}
