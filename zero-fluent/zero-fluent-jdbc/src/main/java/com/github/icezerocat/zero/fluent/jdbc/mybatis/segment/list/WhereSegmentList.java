package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.list;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.If;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag;

import java.util.ArrayList;
import java.util.List;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.isBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column.columnEquals;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Fragments.SEG_EMPTY;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Fragments.SEG_SPACE;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.SPACE;

/**
 * 普通片段
 *
 * @author darui.wu
 */
public class WhereSegmentList extends BaseSegmentList {

    /**
     * 条件中用到的列名称列表
     */
    private final List<Column> columns = new ArrayList<>();

    public WhereSegmentList() {
        super.segments.setDelimiter(SPACE).setFilter(NOT_ONLY_KEY);
    }

    @Override
    public WhereSegmentList add(KeyFrag keyword, IFragment... segments) {
        if (If.isEmpty(segments)) {
            return this;
        }
        IFragment merged = this.segments.isEmpty() ? SEG_EMPTY : keyword;
        boolean needSpace = false;
        for (IFragment seg : segments) {
            if (needSpace) {
                merged = merged.plus(SEG_SPACE);
            } else {
                needSpace = true;
            }
            merged = merged.plus(seg);
            if (seg instanceof Column) {
                columns.add((Column) seg);
            }
        }
        super.segments.add(merged);
        return this;
    }

    /**
     * 示例: column1 = ? AND column2 = ?
     *
     * @return sql
     */
    @Override
    public IFragment get() {
        return super.merge(SEG_EMPTY);
    }

    /**
     * 清空where条件设置
     */
    public void clear() {
        this.segments.clear();
    }

    public boolean containColumn(String column) {
        if (isBlank(column)) {
            return false;
        }
        for (Column segment : this.columns) {
            if (columnEquals(segment, column)) {
                return true;
            }
        }
        return false;
    }
}
