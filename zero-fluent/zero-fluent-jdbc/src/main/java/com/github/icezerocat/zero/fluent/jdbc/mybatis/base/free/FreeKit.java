package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.BaseWrapper;

public interface FreeKit {
    /**
     * 桥接方法
     *
     * @param query BaseWrapper
     * @return FreeQuery
     */
    static FreeQuery newFreeQuery(BaseWrapper query) {
        return new FreeQuery(query.table(false), query.getTableAlias());
    }
}
