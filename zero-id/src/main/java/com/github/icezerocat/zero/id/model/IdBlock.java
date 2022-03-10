package com.github.icezerocat.zero.id.model;

import github.com.icezerocat.jdbctemplate.annotations.TableName;

import java.io.Serializable;

/**
 * Description: id块
 * CreateDate:  2021/11/30 14:25
 *
 * @author zero
 * @version 1.0
 */
@TableName("t_ap_serial_number")
@SuppressWarnings("unused")
public class IdBlock implements Serializable {

    private static final long serialVersionUID = 9133302613147228830L;

    /**
     * 主键
     */
    private Long id;
    /**
     * idName可表示某一天，groupName可表示某一时间段（利于数据统计）
     */
    private String idName;
    /**
     * 开始值
     */
    private long idValue = 1L;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 下一次值
     */
    private long nextId = 2L;
    /**
     * 现在值
     */
    private long lastId = 1L;

    //TODO 字段（模板编码、模板表达式、缓存模式）

    public IdBlock() {
    }

    public IdBlock(String idName) {
        this.idName = idName;
        this.groupName = "default";
    }

    public IdBlock(long nextId, long lastId) {
        this.nextId = nextId;
        this.lastId = lastId;
    }

    public IdBlock(String idName, String finalGroupName) {
        this.idName = idName;
        this.groupName = finalGroupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdName() {
        return this.idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public long getIdValue() {
        return this.idValue;
    }

    public void setIdValue(long idValue) {
        this.idValue = idValue;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getNextId() {
        return this.nextId;
    }

    public void setNextId(long nextId) {
        this.nextId = nextId;
    }

    public long getLastId() {
        return this.lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    @Override
    public String toString() {
        return "IdBlock{" +
                "id=" + id +
                ", idName='" + idName + '\'' +
                ", idValue=" + idValue +
                ", groupName='" + groupName + '\'' +
                ", nextId=" + nextId +
                ", lastId=" + lastId +
                '}';
    }
}
