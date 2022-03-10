package com.github.icezerocat.zero.jdbc.builder.spring;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.SelectSQLBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author dragons
 * @date 2021/12/10 16:07
 */
public interface BaseMapper<T> {

    int insert(T entity);

    List<T> selectList(SQLBuilder wrapper, SelectSQLBuilder selectSqlBuilder);

    List<T> selectList(SQLBuilder wrapper);

    int delete(Serializable... primaries);

    int delete(SQLBuilder wrapper);

    int update(T entity, SQLBuilder wrapper);
}
