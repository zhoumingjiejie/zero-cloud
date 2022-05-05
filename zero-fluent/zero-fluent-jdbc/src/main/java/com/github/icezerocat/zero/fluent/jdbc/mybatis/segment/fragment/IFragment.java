package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment;


import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;

import java.io.Serializable;

/**
 * SQL 片段接口
 *
 * @author darui.wu
 */
@FunctionalInterface
public interface IFragment extends Serializable {

    /**
     * SQL 片段
     *
     * @return ignore
     */
    String get(IMapping mapping);

    default IFragment plus(IFragment segment) {
        return AppendFlag.set(this).append(segment);
    }

    default IFragment plus(String segment) {
        return AppendFlag.set(this).append(segment);
    }

    default boolean notEmpty() {
        return true;
    }
}
