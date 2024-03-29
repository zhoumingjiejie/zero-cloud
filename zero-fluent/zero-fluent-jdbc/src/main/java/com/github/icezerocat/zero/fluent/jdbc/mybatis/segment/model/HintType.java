package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model;

/**
 * 在sql语句特定位置中插入特定语句或注释
 *
 * @author darui.wu
 */
public enum HintType {
    /**
     * hint select ...
     * hint update ...
     * hint delete ...
     */
    Before_All,
    /**
     * select hint ... from table ...
     * update hint table set ....
     * delete hint from table ...
     */
    After_CrudKey,
    /**
     * select ... from hint table ...
     * update hint table set ...
     * delete from hint table(...)
     */
    Before_Table,
    /**
     * select ... from table hint ...
     * update table hint set ...
     * delete from table hint(...)
     */
    After_Table
}
