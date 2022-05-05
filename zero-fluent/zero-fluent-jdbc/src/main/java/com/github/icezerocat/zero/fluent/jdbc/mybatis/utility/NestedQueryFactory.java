package com.github.icezerocat.zero.fluent.jdbc.mybatis.utility;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free.FreeQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.exception.FluentMybatisException;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.BaseWrapper;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free.FreeKit.newFreeQuery;

/**
 * NestedQueryFactory
 *
 * @author darui.wu 2020/6/19 8:34 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NestedQueryFactory {
    /**
     * 构造查询对象
     *
     * @param wrapper 嵌套查询对象类
     * @return 嵌套查询对象
     */
    public static <Q extends IBaseQuery> Q nested(BaseWrapper wrapper, boolean sameAlias) {
        if (wrapper instanceof FreeQuery) {
            FreeQuery query = newFreeQuery(wrapper);
            return (Q) query;
        }
        IMapping mapping = RefKit.byEntity(wrapper.getEntityClass());
        if (mapping == null) {
            throw new FluentMybatisException("create nested Query[" + wrapper.getClass().getName() + "] error.");
        }
        if (sameAlias) {
            return mapping.emptyQuery(wrapper::getTableAlias);
        } else {
            return mapping.emptyQuery();
        }
    }
}
