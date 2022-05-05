package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.op;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.ISqlOp;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.SqlOp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 扩展操作符
 *
 * @author wudarui
 */
public class SqlOps {
    /**
     * 扩展操作符
     */
    private final static List<ISqlOp> EXT_OPS = new ArrayList<>();
    /**
     * postgresql引擎,忽略大小写搜索英文
     */
    public final static ILike ILike = new ILike();

    static {
        register(ILike);
    }

    /**
     * 注册新的自定义操作符
     *
     * @param sqlOp ISqlOp
     */
    public static void register(ISqlOp sqlOp) {
        EXT_OPS.add(sqlOp);
    }

    /**
     * 返回匹配的操作符实例
     *
     * @param op name of ISqlOp
     * @return ISqlOp
     */
    public static ISqlOp get(String op) {
        try {
            return SqlOp.valueOf(op);
        } catch (IllegalArgumentException e) {
            for (ISqlOp item : EXT_OPS) {
                if (Objects.equals(item.name(), op)) {
                    return item;
                }
            }
            throw e;
        }
    }
}
