package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IWrapper;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.JoiningFrag;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * 追加更新默认值
 *
 * @author wudarui
 */
public class UpdateDefault {
    /**
     * 待追加更新默认值列表
     */
    @Getter
    private final JoiningFrag updateDefaults = JoiningFrag.get(", ");
    /**
     * 显式指定的更新字段列表
     */
    private final Set<IFragment> existsColumn;

    public UpdateDefault(Map<IFragment, String> updates) {
        this.existsColumn = updates.keySet();
    }

    /**
     * 增加待追加更新的默认值
     * 如果没有显式指定更新，则追加更新默认值
     *
     * @param field    字段
     * @param _default 默认值
     * @return UpdateDefault
     */
    public UpdateDefault add(IWrapper wrapper, FieldMapping field, String _default) {
        Column column = Column.set(wrapper, field);
        if (!existsColumn.contains(column)) {
            updateDefaults.add(column.plus(" = ").plus(_default));
        }
        return this;
    }
}
