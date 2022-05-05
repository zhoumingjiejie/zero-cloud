package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.If;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.FieldPredicate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IAggregate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IGetter;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.IFragment;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.JoiningFrag;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.LambdaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.If.notBlank;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.COMMA_SPACE;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.StrConstant.COUNT_ASTERISK;
import static java.util.stream.Collectors.toList;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu  2020/6/21 3:13 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class SelectorBase<
    S extends SelectorBase<S, Q>,
    Q extends IBaseQuery<?, Q>
    >
    extends AggregateSegment<S, Q, S> {

    protected SelectorBase(Q query) {
        super(query);
    }

    protected SelectorBase(S origin, IAggregate aggregate) {
        super(origin, aggregate);
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(String column, String... columns) {
        this.assertColumns(columns);
        this.applyAs(column, null);
        Stream.of(columns).forEach(c -> this.applyAs(c, null));
        return super.getOrigin();
    }

    /**
     * 增加查询字段
     *
     * @param fields 查询字段(Entity属性名称)
     * @return 查询字段选择器
     */
    public S applyField(String field, String... fields) {
        this.assertColumns(fields);
        this.applyColumn(this.aggregate, this.column(field), null);
        Stream.of(fields).forEach(c -> this.applyColumn(this.aggregate, this.column(c), null));
        return super.getOrigin();
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(FieldMapping column, FieldMapping... columns) {
        this.assertColumns(columns);
        this.applyAs(column, null);
        Stream.of(columns).forEach(c -> this.applyAs(c, null));
        return super.getOrigin();
    }

    /**
     * 增加查询字段
     *
     * @param getters 查询字段
     * @return 查询字段选择器
     */
    public <E> S apply(IGetter<E> getter, IGetter<E>... getters) {
        this.assertColumns(getters);
        this.applyColumn(this.aggregate, this.column(LambdaUtil.resolveGetter(getter)), null);
        Stream.of(getters).forEach(c -> this.applyColumn(this.aggregate, this.column(LambdaUtil.resolveGetter(c)), null));
        return super.getOrigin();
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(final String column, final String alias) {
        return this.applyColumn(this.aggregate, Column.set(this.wrapper, column), alias);
    }

    /**
     * 增加带别名的查询字段
     *
     * @param getter 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public <E> S applyAs(final IGetter<E> getter, final String alias) {
        return this.applyColumn(this.aggregate, this.column(LambdaUtil.resolveGetter(getter)), alias);
    }

    /**
     * 增加带别名的查询字段
     *
     * @param field 查询字段(Entity属性名)
     * @param alias 别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyFieldAs(final String field, final String alias) {
        return this.applyColumn(this.aggregate, this.column(field), alias);
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(final FieldMapping column, final String alias) {
        return this.applyColumn(this.aggregate, Column.set(this.wrapper, column), alias);
    }

    /**
     * 排除查询字段(按数据库字段名称), 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public Q exclude(String... columns) {
        this.assertColumns(columns);
        List<String> excludes = Arrays.asList(columns);
        IFragment seg = this.excludeSelect(excludes, true);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    /**
     * 排除查询字段(按数据库字段名称), 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public Q excludeField(String... columns) {
        this.assertColumns(columns);
        List<String> excludes = Arrays.asList(columns);
        IFragment seg = this.excludeSelect(excludes, false);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    /**
     * 排除查询字段, 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public <E> Q exclude(IGetter<E>... columns) {
        this.assertColumns(columns);
        List<String> excludes = Stream.of(columns).map(LambdaUtil::resolveGetter).collect(toList());
        IFragment seg = this.excludeSelect(excludes, false);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    /**
     * 排除查询字段, 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public Q exclude(FieldMapping... columns) {
        this.assertColumns(columns);
        List<String> excludes = Stream.of(columns).map(f -> f.column).collect(toList());
        IFragment seg = this.excludeSelect(excludes, true);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    public S applyFunc(final IAggregate func, final String column, final String alias) {
        return this.applyColumn(func, Column.set(this.wrapper, column), alias);
    }

    public S applyFuncByField(final IAggregate func, final String field, final String alias) {
        return this.applyColumn(func, this.column(field), alias);
    }

    public <E> S applyFunc(final IAggregate func, final IGetter<E> getter, final String alias) {
        return this.applyColumn(func, this.column(LambdaUtil.resolveGetter(getter)), alias);
    }

    public S applyFunc(final IAggregate func, final FieldMapping column, final String alias) {
        return this.applyColumn(func, Column.set(this.wrapper, column), alias);
    }

    /**
     * count(*) as alias
     *
     * @param alias 别名, 为空时没有别名
     * @return 选择器
     */
    public S count(String alias) {
        return this.applyAs(COUNT_ASTERISK, alias);
    }

    /**
     * 过滤查询的字段信息
     *
     * <p>例1: 只要 java 字段名以 "test" 开头的   -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 要全部字段                        -> select(i -> true)</p>
     * <p>例3: 只要字符串类型字段                 -> select(i -> i.getPropertyType instance String)</p>
     *
     * @param predicate 过滤方式 (主键除外!)
     * @return 字段选择器
     */
    public S apply(FieldPredicate predicate) {
        List<String> columns = this.wrapper.getTableMeta().filter(false, predicate);
        columns.forEach(this::apply);
        return super.getOrigin();
    }

    /* ================= 非PUBLIC方法 ====================== */
    @Override
    protected S apply() {
        return this.applyAs(this.current.column, null);
    }

    /**
     * 对当前字段处理，别名处理
     *
     * @param field 字段
     * @param alias 别名
     * @return 选择器
     */
    protected S process(FieldMapping field, String alias) {
        this.current = field;
        return this.applyAs(field, alias);
    }

    private static final String AS = " AS ";

    /**
     * 排除查询的字段列表
     *
     * @param excludes 排除查询的字段列表
     * @return ignore
     */
    private IFragment excludeSelect(List<String> excludes, boolean byColumnName) {
        return m -> {
            JoiningFrag joining = new JoiningFrag(COMMA_SPACE);
            for (FieldMapping f : m.allFields()) {
                if (byColumnName && excludes.contains(f.column)) {
                    continue;
                } else if (!byColumnName && excludes.contains(f.name)) {
                    continue;
                }
                joining.add(Column.set(this.wrapper, f));
            }
            return joining.get(m);
        };
    }

    /**
     * 添加要查询的字段
     *
     * @param aggregate 聚合函数
     * @param column    字段(column name or fieldMapping)
     * @param alias     别名
     * @return ignore
     */
    private S applyColumn(IAggregate aggregate, IFragment column, String alias) {
        if (aggregate != null) {
            column = aggregate.aggregate(column);
        }
        if (notBlank(alias)) {
            this.data().getFieldAlias().add(alias);
            column = column.plus(AS).plus(alias);
        }
        this.data().select(column);
        return super.getOrigin();
    }

    private IFragment column(String fieldName) {
        return m -> {
            FieldMapping f = m.getFieldsMap().get(fieldName);
            if (f == null) {
                throw new RuntimeException("Field[" + fieldName + "] of Entity[" + m.entityClass().getName() + "] not defined.");
            } else {
                return Column.set(this.wrapper, f).get(m);
            }
        };
    }

    private void assertColumns(Object[] columns) {
        if (If.notEmpty(columns) && this.aggregate != null) {
            throw new RuntimeException("Aggregate functions allow only one apply column.");
        }
    }
}
