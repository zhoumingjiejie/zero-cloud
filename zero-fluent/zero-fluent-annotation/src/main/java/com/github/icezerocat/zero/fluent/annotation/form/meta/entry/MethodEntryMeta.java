package com.github.icezerocat.zero.fluent.annotation.form.meta.entry;

import com.github.icezerocat.zero.fluent.annotation.form.annotation.EntryType;
import com.github.icezerocat.zero.fluent.annotation.form.meta.EntryMeta;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * 反射方法构造的EntryMeta
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public class MethodEntryMeta extends EntryMeta {
    private final Method getter;

    private final Method setter;

    public MethodEntryMeta(String name, EntryType type, Method getter, Method setter, boolean ignoreNull) {
        super(name, getJavaType(getter, setter), type, ignoreNull);
        this.getter = getter;
        this.setter = setter;
    }

    /**
     * 解析entry属性类型
     *
     * @return java class
     */
    private static Type getJavaType(Method getter, Method setter) {
        if (getter != null) {
            return getter.getGenericReturnType();
        } else if (setter != null) {
            Parameter p = setter.getParameters()[0];
            return p.getParameterizedType();
        } else {
            return null;
        }
    }

    @Override
    protected Function getter() {
        return target -> {
            if (getter == null || target == null) {
                return null;
            }
            try {
                return getter.invoke(target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }

    @Override
    protected BiConsumer setter() {
        return (target, value) -> {
            if (setter == null || target == null) {
                return;
            }
            try {
                setter.invoke(target, value);
            } catch (IllegalArgumentException e) {
                String err = "method:" + setter + ", value " + (value == null ? "<null>" : value.getClass()) + ":" + value;
                throw new IllegalArgumentException(err, e);
            } catch (Exception e) {
                throw wrap(e);
            }
        };
    }
}
