package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IAggregate;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model.Aggregate.*;

/**
 * 聚合函数
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked"})
public abstract class AggregateSegment<
    S extends AggregateSegment<S, Q, R>,
    Q extends IBaseQuery<?, Q>,
    R
    >
    extends BaseSegment<R, Q> {
    public final S and = (S) this;
    /**
     * 聚合对象max,min,sum...等实例的原始实例来源(aggregate=null)
     */
    protected S origin;

    protected final IAggregate aggregate;

    public S max;

    public S min;

    public S sum;

    public S avg;

    public S count;

    public S group_concat;

    protected AggregateSegment(Q query) {
        super(query);
        this.aggregate = null;
        this.max = this.aggregateSegment(MAX);
        this.min = this.aggregateSegment(MIN);
        this.sum = this.aggregateSegment(SUM);
        this.avg = this.aggregateSegment(AVG);
        this.count = this.aggregateSegment(COUNT);
        this.group_concat = this.aggregateSegment(GROUP_CONCAT);
        this.init(max)
            .init(min)
            .init(sum)
            .init(avg)
            .init(count)
            .init(group_concat);
    }

    protected AggregateSegment(S origin, IAggregate aggregate) {
        super((Q) origin.wrapper);
        this.aggregate = aggregate;
        this.origin = origin;
    }

    S init(S selector) {
        selector.max = this.max;
        selector.min = this.min;
        selector.sum = this.sum;
        selector.avg = this.avg;
        selector.count = this.count;
        selector.group_concat = this.group_concat;
        return (S) this;
    }

    /**
     * 构造聚合选择器
     *
     * @param aggregate 聚合函数接口
     * @return S
     */
    protected abstract S aggregateSegment(IAggregate aggregate);

    protected S getOrigin() {
        return this.aggregate == null || this.origin == null ? (S) this : this.origin;
    }
}
