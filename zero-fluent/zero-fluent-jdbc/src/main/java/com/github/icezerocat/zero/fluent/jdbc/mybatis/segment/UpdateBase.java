package com.github.icezerocat.zero.fluent.jdbc.mybatis.segment;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IBaseUpdate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IGetter;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.fragment.Column;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MappingKits;

import java.util.Arrays;
import java.util.Map;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * BaseSetter: 更新设置操作
 *
 * @param <S> 更新器
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class UpdateBase<
    S extends UpdateBase<S, U>,
    U extends IBaseUpdate<?, U, ?>
    >
    extends BaseSegment<UpdateApply<S, U>, U> {

    private final UpdateApply<S, U> apply = new UpdateApply<>((S) this);

    public final S set = (S) this;

    protected UpdateBase(U updater) {
        super(updater);
    }

    /**
     * 按照values中非null值更新记录
     *
     * @param values key-value条件
     * @return self
     */
    public S byNotNull(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> {
                Column _column = Column.set(this.wrapper, column);
                this.data().updateSet(_column, value);
            });
        }
        return (S) this;
    }

    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param columns 要更新的字段
     * @return self
     */
    public S byEntity(IEntity entity, FieldMapping column, FieldMapping... columns) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(column, columns);
        return this.byEntity(entity, arr);
    }

    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param getters 要更新的字段, Entity::getter函数
     * @return self
     */
    public <E extends IEntity> S byEntity(E entity, IGetter<E> getter, IGetter<E>... getters) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns((Class) entity.entityClass(), getter, getters);
        return this.byEntity(entity, arr);
    }


    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param columns 要更新的字段
     * @return self
     */
    public S byEntity(IEntity entity, String... columns) {
        super.byEntity(entity, (column, value) -> {
            Column _column = Column.set(this.wrapper, column);
            this.data().updateSet(_column, value);
        }, false, Arrays.asList(columns));
        return (S) this;
    }

    /**
     * 根据entity字段(包括null字段), 但排除指定字段
     *
     * @param entity   实例
     * @param excludes 排除更新的字段
     * @return self
     */
    public S byExclude(IEntity entity, FieldMapping exclude, FieldMapping... excludes) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(exclude, excludes);
        return this.byExclude(entity, arr);
    }

    /**
     * 根据entity字段(包括null字段), 但排除指定字段
     *
     * @param entity   实例
     * @param excludes 排除更新的字段, Entity::getter函数
     * @return self
     */
    public <E extends IEntity> S byExclude(E entity, IGetter<E> exclude, IGetter<E>... excludes) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns((Class) entity.entityClass(), exclude, excludes);
        return this.byExclude(entity, arr);
    }

    /**
     * 更新除指定的排除字段外其它entity字段值(包括null字段)
     * <p>
     * 无排除字段时, 更新除主键外其它字段(包括null值字段)
     *
     * @param entity   实例
     * @param excludes 排除更新的字段
     * @return self
     */
    public S byExclude(IEntity entity, String... excludes) {
        super.byExclude(entity, (column, value) -> {
            Column _column = Column.set(this.wrapper, column);
            this.data().updateSet(_column, value);
        }, false, Arrays.asList(excludes));
        return (S) this;
    }

    @Override
    protected UpdateApply<S, U> apply() {
        return apply;
    }
}
