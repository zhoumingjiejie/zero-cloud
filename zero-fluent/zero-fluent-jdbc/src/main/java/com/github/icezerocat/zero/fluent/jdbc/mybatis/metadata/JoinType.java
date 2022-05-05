package com.github.icezerocat.zero.fluent.jdbc.mybatis.metadata;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.SPACE;

/**
 * 关联查询类型
 */
public enum JoinType implements IFragment {
    /**
     * inner join
     */
    Join("JOIN"),
    /**
     * left join
     */
    LeftJoin("LEFT JOIN"),
    /**
     * right join
     */
    RightJoin("RIGHT JOIN");

    private final String join;

    JoinType(String join) {
        this.join = join;
    }

    @Override
    public String get(IMapping mapping) {
        return this.join + SPACE;
    }
}
