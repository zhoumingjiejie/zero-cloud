package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.list;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.If;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag.GROUP_BY;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.COMMA_SPACE;

/**
 * Group By SQL 片段
 *
 * @author darui.wu
 */
public class GroupBySegmentList extends BaseSegmentList {

    public GroupBySegmentList() {
        super.segments.setDelimiter(COMMA_SPACE).setFilter(If::notBlank);
    }

    @Override
    public BaseSegmentList add(KeyFrag keyword, IFragment... sqlSegments) {
        super.segments.add(sqlSegments);
        return this;
    }

    /**
     * 示例: group by column1, column2
     *
     * @return sql
     */
    @Override
    public IFragment get() {
        return super.merge(GROUP_BY);
    }
}
