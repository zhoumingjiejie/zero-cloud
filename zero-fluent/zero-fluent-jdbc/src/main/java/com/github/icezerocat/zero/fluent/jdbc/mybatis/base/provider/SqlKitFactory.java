package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.provider;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.intf.IHasDbType;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.intf.IHasMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.metadata.DbType;

import java.util.HashMap;
import java.util.Map;

/**
 * SqlKit工厂类
 *
 * @author darui.wu
 */
public class SqlKitFactory {
    private static SqlKitFactory DEFAULT_FACTORY = new SqlKitFactory();

    public static SqlKit factory(DbType dbType) {
        return DEFAULT_FACTORY.sqlKit(dbType);
    }

    public static SqlKit factory(IHasMapping obj) {
        return factory(obj.mapping());
    }

    public static SqlKit factory(IHasDbType mapping) {
        return factory(mapping.db());
    }

    protected SqlKitFactory() {
    }

    /**
     * 改变默认的SqlKit工厂实现
     *
     * @param factory SqlKitFactory
     */
    public static void setFactory(SqlKitFactory factory) {
        DEFAULT_FACTORY = factory;
    }

    /**
     * 工具类单例
     */
    protected final Map<DbType, SqlKit> kits = new HashMap<>(8);

    public SqlKit sqlKit(DbType dbType) {
        SqlKit kit = kits.get(dbType);
        return kit == null ? this.newSqlKit(dbType) : kit;
    }

    private synchronized SqlKit newSqlKit(DbType dbType) {
        if (kits.containsKey(dbType)) {
            return kits.get(dbType);
        }
        switch (dbType) {
            case ORACLE:
            case ORACLE12:
                kits.put(dbType, new OracleSqlKit());
                break;
            default:
                kits.put(dbType, new CommonSqlKit());
        }
        return kits.get(dbType);
    }
}
