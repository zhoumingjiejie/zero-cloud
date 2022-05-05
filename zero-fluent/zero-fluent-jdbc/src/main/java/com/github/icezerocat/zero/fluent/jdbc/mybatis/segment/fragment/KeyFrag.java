package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.SPACE;

/**
 * KeyWordSegment
 *
 * @author darui.wu 2020/6/20 8:35 下午
 */
public enum KeyFrag implements IFragment {
    /**
     * SELECT
     */
    SELECT("SELECT"),

    DISTINCT("DISTINCT"),

    INSERT_INTO("INSERT INTO"),

    DELETE("DELETE"),

    UPDATE("UPDATE"),

    SET("SET"),
    /**
     * FROM
     */
    FROM("FROM"),
    /**
     * WHERE
     */
    WHERE("WHERE"),
    /**
     * GROUP BY标识
     */
    GROUP_BY("GROUP BY"),
    /**
     * HAVING标识
     */
    HAVING("HAVING"),
    /**
     * ORDER BY标识
     */
    ORDER_BY("ORDER BY"),
    /**
     * and
     */
    AND("AND"),
    /**
     * or
     */
    OR("OR");

    /**
     * 代码片段
     */
    private final String keyWord;

    KeyFrag(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String get(IMapping mapping) {
        return this.keyWord + SPACE;
    }

    @Override
    public String toString() {
        return this.keyWord;
    }

    public String key() {
        return this.keyWord;
    }
}
