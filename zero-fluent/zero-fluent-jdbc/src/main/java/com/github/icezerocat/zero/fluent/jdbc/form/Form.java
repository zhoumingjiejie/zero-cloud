package com.github.icezerocat.zero.fluent.jdbc.form;

import com.github.icezerocat.zero.fluent.jdbc.form.kits.GsonKit;
import com.github.icezerocat.zero.fluent.jdbc.form.setter.*;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IUpdate;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.entity.IMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.mapper.IRichMapper;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.model.FieldMapping;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.IGetter;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.mapper.FluentConst;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.model.StdPagedList;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.model.TagPagedList;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.LambdaUtil;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.PoJoHelper;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.RefKit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * 简单表单查询设置
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(chain = true)
public class Form implements Serializable {
    private static final long serialVersionUID = 7702717917894301362L;

    private transient Class entityClass;

    /**
     * 添加条件项
     */
    @Getter(AccessLevel.NONE)
    public final transient FormItemAdder and = new FormItemAdder(this);

    /**
     * 更新(插入)信息
     */
    @Getter(AccessLevel.NONE)
    private Map<String, Object> update = new HashMap<>();

    public Map<String, Object> getUpdate() {
        if (this.update == null) {
            this.update = new HashMap<>();
        }
        return this.update;
    }

    /**
     * 条件项列表
     */
    @Getter(AccessLevel.NONE)
    private List<FormEntry> where = new ArrayList<>();

    public List<FormEntry> getWhere() {
        if (this.where == null) {
            this.where = new ArrayList<>();
        }
        return where;
    }

    /**
     * 排序项
     */
    @Getter(AccessLevel.NONE)
    private List<FormItemOrder> order = new ArrayList<>();

    public List<FormItemOrder> getOrder() {
        if (this.order == null) {
            this.order = new ArrayList<>();
        }
        return order;
    }

    /**
     * 标准分页时, 当前页码
     */
    private Integer currPage;

    /**
     * Tag分页时(当前页id起始值)
     * 更新操作时 (当前记录id值)
     */
    private String id;

    public Form setId(Number id) {
        this.id = String.valueOf(id);
        return this;
    }

    public Form setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 查询一页的数量
     */
    private int pageSize = 1;

    public Form() {
    }

    public <E extends IEntity> Form(Class<E> eClass) {
        this.entityClass = eClass;
    }

    public <E extends IEntity> Form set(IGetter<E> getter, Object value) {
        String field = LambdaUtil.resolveGetter(getter);
        return this.set(field, value);
    }

    public Form set(String field, Object value) {
        this.getUpdate().put(field, value);
        return this;
    }

    /**
     * 添加IEntity字段提取和实例
     *
     * @param apply IEntity字段值提取器
     * @param value IEntity实例
     * @return IFormApply
     */
    public Form apply(Object value, Consumer<IFormApply> apply) {
        assertNotNull("object", value);
        Map map = PoJoHelper.toMap(value);
        IFormApply formApply = new FormApply(new FormSetter(RefKit.byEntity(entityClass)), map, this);
        apply.accept(formApply);
        return this;
    }

    /**
     * 标准分页时, 设置分页参数
     *
     * @param currPage 当前页码号
     * @param pageSize 每页记录数
     */
    public Form setPage(int currPage, int pageSize) {
        this.currPage = currPage;
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 增加排序条件
     *
     * @param field 排序字段
     * @param asc   是否正序
     * @return ignore
     */
    public Form orderBy(FieldMapping field, boolean asc) {
        this.getOrder().add(new FormItemOrder(field.name, asc));
        return this;
    }

    /**
     * 按表单条件查询记录列表
     *
     * @param <E> 实例类型
     * @return 实例列表
     */
    public <E extends IEntity> List<E> list() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.listEntity(query);
    }

    /**
     * 按表单统计符合条件的记录数
     *
     * @return 符合条件的记录数
     */
    public int count() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.count(query);
    }

    /**
     * 按表单返回标准分页记录
     *
     * @param <E> 实例类型
     * @return 标准分页记录
     */
    public <E extends IEntity> StdPagedList<E> stdPage() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.stdPagedEntity(query);
    }

    /**
     * 按表单返回Tag分页记录
     *
     * @param <E> 实例类型
     * @return Tag分页记录
     */
    public <E extends IEntity> TagPagedList<E> tagPage() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.tagPagedEntity(query);
    }

    /**
     * 按表单返回一条记录
     *
     * @param <E> 实例类型
     * @return 返回的实例
     */
    public <E extends IEntity> E findOne() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        query.limit(1);
        return (E) mapper.findOne(query);
    }

    /**
     * 按表单更新记录
     *
     * @return 更新的记录数
     */
    public int update() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IUpdate update = FormHelper.toUpdate(this.entityClass, this);
        return mapper.updateBy(update);
    }

    /**
     * 按表单新增记录
     *
     * @return 新增的实体实例
     */
    public <E extends IEntity> E insert() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IEntity entity = RefKit.byEntity(this.entityClass).toEntity(this.getUpdate());
        mapper.insert(entity);
        return (E) entity;
    }

    /**
     * 根据表单条件物理删除
     *
     * @return 物理删除记录数
     */
    public int delete() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.delete(query);
    }

    /**
     * 根据表单条件逻辑删除
     *
     * @return 逻辑删除记录数
     */
    public int logicDelete() {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, this.entityClass);
        IRichMapper mapper = RefKit.mapper(this.entityClass);
        IQuery query = FormHelper.toQuery(this.entityClass, this);
        return mapper.logicDelete(query);
    }

    /* =========================static============================== */

    public static <E extends IEntity> Form with(Class<E> eClass, String json) {
        Form form = GsonKit.form(json);
        form.setEntityClass(eClass);
        return form;
    }

    public static <E extends IEntity> Form with(E o, Consumer<IFormApply> apply) {
        Map map = PoJoHelper.toMap(o);
        IMapping mapping = RefKit.byEntity(o.entityClass());
        FormApply formApply = new FormApply(new FormSetter(mapping), map, new Form(o.entityClass()));
        apply.accept(formApply);
        return formApply.getForm();
    }

    public static <E extends IEntity> Form with(Class<E> eClass, Object o, Consumer<IFormApply> apply) {
        Map map = PoJoHelper.toMap(o);
        IMapping mapping = RefKit.byEntity(eClass);
        FormApply formApply = new FormApply(new FormSetter(mapping), map, new Form(eClass));
        apply.accept(formApply);
        return formApply.getForm();
    }
}
