package com.github.icezerocat.zero.fluent.jdbc.mybatis.ifs;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.ISqlOp;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.ObjectArray;

import java.util.Arrays;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * 值对结构
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class IfsPredicate {
    public final Predicate predicate;

    private final Object[] values;

    public IfsPredicate(Predicate predicate, Object... values) {
        this.predicate = predicate;
        this.values = convert(values);
    }

    private Object[] convert(Object[] values) {
        if (values == null || values.length != 1) {
            return values;
        }
        Object first = values[0];
        if (first == null) {
            return values;
        } else if (first.getClass().isArray()) {
            return ObjectArray.array(first);
        } else {
            return values;
        }
    }

    public Object value(ISqlOp op) {
        if (op == null || values == null) {
            return this.firstValue();
        }
        if (SqlOp.IN.equals(op)) {
            return Arrays.stream(values).collect(toList());
        } else {
            return this.firstValue();
        }
    }

    private Object firstValue() {
        return values == null || values.length == 0 ? null : values[0];
    }
}
