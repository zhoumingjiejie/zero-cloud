package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IWrapper;

/**
 * JoinQueryWhere
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface JoinSegment {

    class QueryWhere extends WhereBase {
        protected QueryWhere(JoinQuery wrapper) {
            super(wrapper);
        }

        protected QueryWhere(JoinQuery wrapper, QueryWhere and) {
            super(wrapper, and);
        }

        @Override
        protected QueryWhere buildOr(WhereBase and) {
            return new QueryWhere((JoinQuery) this.wrapper, (QueryWhere) and);
        }
    }

    class OrderBy extends OrderByBase {
        protected OrderBy(IWrapper wrapper) {
            super(wrapper);
        }
    }
}
