package com.github.icezerocat.zero.fluent.annotation.kits;

import java.lang.reflect.Type;
import java.util.*;

/**
 * key值为String的HashMap对象
 *
 * @param <T> value值类型
 * @author wudarui
 */
public class KeyMap<T> {
    private Map<String, T> map;

    public KeyMap() {
        this.map = new HashMap<>();
    }

    public T get(Type klass) {
        return this.map.get(klass.getTypeName());
    }

    public T get(String key) {
        return this.map.get(key);
    }

    public KeyMap<T> put(Type klass, T value) {
        this.map.put(klass.getTypeName(), value);
        return this;
    }

    public KeyMap<T> put(String key, T value) {
        this.map.put(key, value);
        return this;
    }

    public boolean containsKey(Type klass) {
        return this.map.containsKey(klass.getTypeName());
    }

    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }

    /**
     * 设置成不可更改的Map实例
     *
     * @return ignore
     */
    public KeyMap<T> unmodified() {
        this.map = Collections.unmodifiableMap(this.map);
        return this;
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public Set<Map.Entry<String, T>> entrySet() {
        return this.map.entrySet();
    }

    public Collection<T> values() {
        return this.map.values();
    }
}
