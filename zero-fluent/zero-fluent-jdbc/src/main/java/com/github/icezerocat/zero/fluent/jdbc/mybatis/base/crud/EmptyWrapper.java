package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.WhereBase;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model.WrapperData;

import java.util.List;
import java.util.Optional;

/**
 * IWrapper空实现
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public class EmptyWrapper implements IWrapper {
    public static final EmptyWrapper INSTANCE = new EmptyWrapper();

    private EmptyWrapper() {
    }

    @Override
    public WhereBase where() {
        throw new RuntimeException("not support.");
    }

    @Override
    public WrapperData data() {
        throw new RuntimeException("not support.");
    }

    @Override
    public IFragment table(boolean notFoundError) {
        throw new RuntimeException("not support.");
    }

    @Override
    public Optional<IMapping> mapping() {
        throw new RuntimeException("not support.");
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("not support.");
    }
}
