package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity;


import cn.org.atool.fluent.mybatis.metadata.DbType;

import java.util.Objects;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.notBlank;

/**
 * 主键信息
 *
 * @author darui.wu
 */
public class TableId {
    /**
     * 属性名
     */
    public final String name;
    /**
     * 字段名
     */
    public final String column;
    /**
     * {@link TableId#auto()}
     */
    public final boolean auto;
    /**
     * {@link TableId#seqName()}
     */
    public final String seqName;
    /**
     * {@link TableId#before()}
     */
    public final boolean before;

    public TableId(String name, String column, boolean auto, String seqName, boolean before) {
        this.name = name;
        this.column = column;
        this.auto = auto;
        this.seqName = seqName;
        this.before = before;
    }

    /**
     * - seqName不为空的情况下且和默认seq不一样
     * - 显式设置
     *
     * @param type 数据库类型
     * @return true/false
     */
    public boolean isSeqBefore(DbType type) {
        return (notBlank(seqName) && !Objects.equals(type.feature.getSeq(), seqName)) || before;
    }
}
