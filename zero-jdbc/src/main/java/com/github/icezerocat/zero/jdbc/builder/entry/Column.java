package com.github.icezerocat.zero.jdbc.builder.entry;

/**
 * @author dragons
 * @date 2021/11/16 18:37
 */
public class Column {

    private final String name;

    protected Column(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Column as(Object column) {
        return new Column(String.valueOf(column));
    }
}
