package com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.mapper.IRichMapper;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.mapper.QueryExecutor;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.BaseWrapper;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.OrderByBase;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.WhereBase;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model.WrapperData;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.RefKit;

import java.util.Optional;
import java.util.function.Function;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.assertNotNull;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.assertTrue;

/**
 * 查询构造器基类
 *
 * @param <E> 实体类型
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface IQuery<E extends IEntity> {
    /**
     * 数据库映射定义
     *
     * @return Optional<IMapping>
     */
    Optional<IMapping> mapping();

    /**
     * distinct 查询
     *
     * @return self
     */
    <Q extends IQuery<E>> Q distinct();

    /**
     * 查询Entity所有字段
     *
     * @return self
     */
    <Q extends IQuery<E>> Q selectAll();

    /**
     * 只查询主键字段
     *
     * @return self
     */
    <Q extends IQuery<E>> Q selectId();

    /**
     * 设置limit值
     *
     * @param limit 最大查询数量
     * @return self
     */
    <Q extends IQuery<E>> Q limit(int limit);

    /**
     * 设置limit值
     *
     * @param start 开始查询偏移量
     * @param limit 最大查询数量
     * @return self
     */
    <Q extends IQuery<E>> Q limit(int start, int limit);

    /**
     * 设置分页参数(会转换为limit入参)
     *
     * @param currPage 当前页码, 从1开始计数
     * @param pageSize 每页记录数
     * @return self
     */
    default <Q extends IQuery<E>> Q paged(int currPage, int pageSize) {
        assertTrue("The currPage must be greater than one.", currPage >= 1);
        assertTrue("The pageSize must be greater than one.", pageSize >= 1);
        return this.limit((currPage - 1) * pageSize, pageSize);
    }

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql 追加SQL
     * @return self
     */
    <Q extends IQuery<E>> Q last(String lastSql);

    /**
     * 返回where
     *
     * @return self
     */
    WhereBase where();

    /**
     * 返回order排序
     *
     * @return ignore
     */
    OrderByBase orderBy();

    /**
     * 返回查询器或更新器对应的xml数据
     * 系统方法, 请勿调用
     *
     * @return self
     */
    WrapperData data();

    /**
     * 根据Query定义执行后续操作
     * <pre>
     *   要使用本方法
     *   需要定义 {@link com.github.icezerocat.zero.fluent.jdbc.mybatis.spring.MapperFactory} spring bean
     *
     *   same as  {@link #of(IRichMapper)} 方法
     *  </pre>
     *
     * @return self
     */
    default QueryExecutor<E> to() {
        Class entityClass = ((BaseWrapper) this).getEntityClass();
        assertNotNull("entity class", entityClass);
        IRichMapper mapper = RefKit.mapper(entityClass);
        return new QueryExecutor<>(mapper, this);
    }

    /**
     * 根据Query定义执行后续操作
     *
     * @param mapper 执行操作的mapper
     * @return QueryExecutor
     */
    default QueryExecutor<E> of(IRichMapper<E> mapper) {
        return new QueryExecutor<>(mapper, this);
    }

    /**
     * 执行查询操作
     *
     * @param executor 具体查询操作
     * @param <R>      结果类型
     * @return 结果
     * @deprecated replaced by {@link #of(IRichMapper).method(...)}
     */
    @Deprecated
    default <R> R execute(Function<IQuery<E>, R> executor) {
        return executor.apply(this);
    }

    /**
     * select * from a where...
     * UNION
     * select * from b where...
     *
     * @param queries 查询条件列表
     * @return ignore
     */
    IQuery union(IQuery... queries);

    /**
     * select * from a where...
     * UNION ALL
     * select * from b where...
     *
     * @param queries 查询条件列表
     * @return ignore
     */
    IQuery unionAll(IQuery... queries);
}
