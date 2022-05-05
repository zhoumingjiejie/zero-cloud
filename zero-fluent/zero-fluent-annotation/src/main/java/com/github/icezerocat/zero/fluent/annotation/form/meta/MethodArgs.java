package com.github.icezerocat.zero.fluent.annotation.form.meta;

import lombok.AllArgsConstructor;

/**
 * 方法元数据+入参实际值
 *
 * @author darui.wu
 */
@AllArgsConstructor
public class MethodArgs {
    public final MethodMeta meta;

    public final Object[] args;

    /**
     * 查询数据接口
     */
    public boolean isQuery() {
        return meta.isQuery();
    }

    /**
     * 更新数据接口
     */
    public boolean isUpdate() {
        return meta.isUpdate();
    }

    /**
     * 创建实例接口
     */
    public boolean isSave() {
        return meta.isSave();
    }

    /**
     * 返回每页记录数
     *
     * @return 每页记录数
     */
    public int getPageSize() {
        return this.meta.metas().getPageSize(args);
    }

    public Integer getCurrPage() {
        return this.meta.metas().getCurrPage(args);
    }

    public Object getPagedTag() {
        return this.meta.metas().getPagedTag(args);
    }
}
