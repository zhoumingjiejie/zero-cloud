package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.RichEntity;

/**
 * 空实体, 仅仅为拼接查询使用
 *
 * @author darui.wu
 */
final class FreeEntity extends RichEntity {
    @Override
    public Class<? extends IEntity> entityClass() {
        return FreeEntity.class;
    }
}
