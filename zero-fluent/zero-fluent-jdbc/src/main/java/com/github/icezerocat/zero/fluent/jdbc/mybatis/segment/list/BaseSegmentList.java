package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.list;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.JoiningFrag;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag;
import lombok.Getter;

import java.util.function.Predicate;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.isBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.notBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag.AND;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.KeyFrag.OR;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.EMPTY;

/**
 * SQL 片段集合 处理的抽象类
 *
 * @author darui.wu
 */
public abstract class BaseSegmentList {
    /**
     * sql片段列表
     */
    @Getter
    public final JoiningFrag segments = JoiningFrag.get();

    public boolean isEmpty() {
        return segments.isEmpty();
    }

    protected String cached;

    public String get(IMapping mapping) {
        if (cached == null) {
            cached = this.get().get(mapping);
        }
        return this.cached;
    }

    public abstract IFragment get();

    /**
     * 添加sql片段
     *
     * @param keyword     关键字
     * @param sqlSegments sql片段列表
     * @return self
     */
    public abstract BaseSegmentList add(KeyFrag keyword, IFragment... sqlSegments);

    /**
     * 合并处理
     *
     * @param keyword 前序关键字
     * @return IFragment
     */
    protected final IFragment merge(IFragment keyword) {
        return m -> {
            String text = this.segments.get(m).trim();
            return isBlank(text) ? EMPTY : keyword.get(m) + text;
        };
    }

    protected static final Predicate<String> NOT_ONLY_KEY = text -> {
        text = text.trim().toUpperCase();
        if (AND.key().equals(text) || OR.key().equals(text)) {
            return false;
        } else if (text.startsWith("(") && text.startsWith(")")) {
            return notBlank(text.substring(1, text.length() - 1));
        } else {
            return true;
        }
    };
}
