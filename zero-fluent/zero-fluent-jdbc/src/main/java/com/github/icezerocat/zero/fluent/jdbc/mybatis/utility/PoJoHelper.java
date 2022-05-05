package com.github.icezerocat.zero.fluent.jdbc.mybatis.utility;

import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.IEntity;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.crud.IQuery;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.base.intf.IToMap;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.exception.FluentMybatisException;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.functions.MapFunction;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.metadata.SetterMeta;
import com.github.icezerocat.zero.fluent.jdbc.mybatis.segment.model.PagedOffset;
import lombok.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.github.icezerocat.zero.fluent.jdbc.mybatis.typehandler.ConvertorKit.convertValueToType;
import static com.github.icezerocat.zero.fluent.jdbc.mybatis.utility.MybatisUtil.assertNotNull;
import static java.util.stream.Collectors.toList;

/**
 * PoJo转换工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class PoJoHelper {
    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param list      Map或Entity对象列表
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象列表
     */
    public static <POJO> List<POJO> toPoJoList(List<Map<String, Object>> list, MapFunction<POJO> converter) {
        return list == null ? null : (List) list.stream().map(map -> toPoJo(map, converter)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param map       map或entity对象
     * @param converter 转换函数
     * @param <POJO>    PoJo类型
     * @return 转换后的对象
     */
    public static <POJO> POJO toPoJo(Map<String, Object> map, @NonNull MapFunction converter) {
        return map == null ? null : (POJO) converter.apply(map);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param list   map对象列表
     * @param <POJO> POJO类型
     * @return POJO实例列表
     */
    public static <POJO> List<POJO> toPoJoList(Class<POJO> clazz, List<Map<String, Object>> list) {
        return list == null ? null : list.stream().map(map -> toPoJo(clazz, map)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param list   map对象列表
     * @param <POJO> POJO类型
     * @return POJO实例列表
     */
    public static <POJO> List<POJO> toPoJoListIgnoreNotFound(Class<POJO> clazz, List<Map<String, Object>> list) {
        return list == null ? null : list.stream().map(map -> toPoJoIgnoreNotFound(clazz, map)).collect(toList());
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    public static <POJO> POJO toPoJo(@NonNull Class<POJO> clazz, @NonNull Map<String, Object> map) {
        return toPoJo(clazz, map, false);
    }

    /**
     * 将Map转换为指定的PoJo对象
     *
     * @param clazz  POJO类型
     * @param map    map对象
     * @param <POJO> PoJo类型
     * @return 根据Map值设置后的对象
     */
    public static <POJO> POJO toPoJoIgnoreNotFound(@NonNull Class<POJO> clazz, @NonNull Map<String, Object> map) {
        return toPoJo(clazz, map, true);
    }

    private static <POJO> POJO toPoJo(@NonNull Class<POJO> klass, @NonNull Map<String, Object> map, boolean ignoreNotFound) {
        POJO target = newInstance(klass);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String name = MybatisUtil.underlineToCamel(entry.getKey(), false);
            SetterMeta meta = SetterMeta.get(klass, name);
            if (meta == null) {
                if (ignoreNotFound) {
                    continue;
                }
                throw new RuntimeException("property[" + name + "] of class[" + klass.getName() + "] not found.");
            }
            try {
                Object value = entry.getValue();
                if (value != null) {
                    Object _value = convertValueToType(value, meta.fType);
                    meta.setValue(target, _value);
                }
            } catch (Exception e) {
                String err = String.format("convert map to object[class=%s, property=%s, type=%s] error: %s",
                    klass.getName(), name, meta.fType.toString(), e.getMessage());
                throw new RuntimeException(err, e);
            }
        }
        return target;
    }

    private static <POJO> POJO newInstance(Class<POJO> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("convert map to object[type=" + clazz.getName() + "] error: " + e.getMessage(), e);
        }
    }

    /**
     * 将object对象转换为map
     *
     * @param object 转换对象
     * @return Map
     */
    public static Map toMap(Object object) {
        assertNotNull("object", object);
        if (object instanceof IEntity) {
            return ((IEntity) object).toEntityMap();
        } else if (object instanceof Map) {
            return new HashMap((Map) object);
        } else if (object instanceof IToMap) {
            return ((IToMap) object).toMap();
        } else {
            return IToMap.toMap(object);
        }
    }

    /**
     * 校验marker方式分页的分页参数合法性
     *
     * @param query 查询条件
     * @return 最大查询数
     */
    public static int validateTagPaged(IQuery query) {
        PagedOffset paged = query.data().paged();
        if (paged == null) {
            throw new FluentMybatisException("Paged parameter not set");
        }
        if (paged.getOffset() != 0) {
            throw new FluentMybatisException("The offset of TagList should from zero, please use method: limit(size) or limit(0, size) .");
        }
        return paged.getLimit();
    }

    /**
     * 返回list对象属性值数组
     *
     * @param list    对象列表
     * @param getFunc 获取list元素对应属性方法
     * @param <T>     转换对象类型
     * @return 属性值数组
     */
    public static <T> Object[] getFields(List<T> list, Function<T, Object> getFunc) {
        return list.stream().map(getFunc).toArray(Object[]::new);
    }
}
