package com.github.icezerocat.zero.fluent.jdbc.form;

import com.github.icezerocat.zero.fluent.annotation.form.annotation.EntryType;
import com.github.icezerocat.zero.fluent.annotation.form.annotation.MethodType;
import com.github.icezerocat.zero.fluent.annotation.form.meta.ArgumentMeta;
import com.github.icezerocat.zero.fluent.annotation.form.meta.MethodMeta;
import com.github.icezerocat.zero.fluent.annotation.form.meta.entry.PagedEntry;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.model.StdPagedList;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.model.TagPagedList;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static com.github.icezerocat.zero.fluent.jdbc.form.registrar.FormServiceKit.TableEntityClass;

/**
 * Form操作辅助类
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unused"})
@Slf4j
public class FormKit {
    private FormKit() {
    }

    /**
     * 定义映射关系
     *
     * @param table       表名称
     * @param entityClass 实例类型
     */
    public static void mapping(String table, Class<? extends IEntity> entityClass) {
        if (TableEntityClass.containsKey(table)) {
            log.warn("Table[{}] and entity {} have been associated, and entity {} can no longer be associated.",
                table, TableEntityClass.get(table), entityClass);
        } else {
            TableEntityClass.put(table, entityClass);
        }
    }

    /**
     * 构造新增记录Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回值类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildSave(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Save, args, returnType, null);
    }

    /**
     * 构造更新Action
     *
     * @param entityClass 操作表Entity类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildUpdate(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Update, args, returnType, null);
    }

    /**
     * 构造单个对象查询(count 或 findOne) Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回的单个对象类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildQuery(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Query, args, returnType, null);
    }

    /**
     * 构造列表查询Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 列表元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildList(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Query, args, List.class, returnParameterType);
    }

    /**
     * 构造标准分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildStdPage(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Query, args, StdPagedList.class, returnParameterType);
    }

    /**
     * 构造tag分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildTagPage(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, MethodType.Query, args, TagPagedList.class, returnParameterType);
    }

    /**
     * 参数为表单项
     *
     * @param methodType 方法类型
     * @param type       参数类型
     * @param index      参数次序
     * @return ArgumentMeta
     */
    public static <T, R> ArgumentMeta argForm(MethodType methodType, Type type, int index, Map types) {
        return new ArgumentMeta(methodType, null, EntryType.Form, type, index, types);
    }

    /**
     * 构建tag分页表单
     *
     * @param index 参数次序
     * @return PagedEntry
     */
    public static <T> ArgumentMeta argPaged(int index, Map types) {
        return argForm(MethodType.Query, PagedEntry.class, index, types);
    }
}
