package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.provider;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.BatchCrudImpl;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IUpdate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.TableId;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;

/**
 * oracle批量插入语法
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public class OracleSqlKit extends CommonSqlKit {
    public OracleSqlKit() {
        super();
    }

    @Override
    public KeyGenerator insert(StatementBuilder builder, FieldMapping primary, TableId tableId) {
        if (this.isAutoKeyGenerator(tableId)) {
            return NoKeyGenerator.INSTANCE;
        } else {
            return builder.handleSelectKey(primary, tableId);
        }
    }

    @Override
    protected boolean isSelectInsert(IMapping mapping, boolean withPk, TableId tableId) {
        return true;
    }

    @Override
    public String batchCrud(IMapping mapping, BatchCrudImpl crud) {
        String sql = crud.batchSql(mapping, this);
        return wrapperBeginEnd(sql);
    }

    @Override
    public String updateBy(IMapping mapping, IUpdate[] updaters) {
        String sql = super.updateBy(mapping, updaters);
        return updaters.length == 1 ? sql : wrapperBeginEnd(sql);
    }

    public static String wrapperBeginEnd(String sql) {
        return "BEGIN " + sql + "; END;";
    }
}
