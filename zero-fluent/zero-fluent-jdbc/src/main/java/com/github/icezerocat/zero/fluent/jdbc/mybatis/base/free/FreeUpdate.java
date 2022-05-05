package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.BaseUpdate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free.FreeSegment.UpdateSetter;
import lombok.experimental.Accessors;

import java.util.List;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free.FreeSegment.UpdateOrderBy;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.base.free.FreeSegment.UpdateWhere;

/**
 * FreeUpdate
 *
 * @author wudarui
 */
@Accessors(chain = true)
public class FreeUpdate extends BaseUpdate<FreeEntity, FreeUpdate, FreeQuery> {
    public final UpdateSetter update = new UpdateSetter(this);

    public final UpdateWhere where = new UpdateWhere(this);

    public final UpdateOrderBy orderBy = new UpdateOrderBy(this);

    public FreeUpdate(String table) {
        super(table, FreeEntity.class);
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("The method is not supported by FreeUpdate.");
    }

    @Override
    public UpdateWhere where() {
        return this.where;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql       用户定义的完整sql语句
     * @param parameter sql参数, 通过#{value} 或 #{field.field}占位
     * @return self
     */
    public FreeUpdate customizedByPlaceholder(String sql, Object parameter) {
        this.data.customizedSql(sql, parameter);
        return this;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql   用户定义的完整sql语句
     * @param paras sql参数, 通过sql中的'?'占位
     * @return self
     */
    public FreeUpdate customizedByQuestion(String sql, Object... paras) {
        String placeholder = this.data.paramSql(null, sql, paras);
        this.data.customizedSql(placeholder, null);
        return this;
    }
}
