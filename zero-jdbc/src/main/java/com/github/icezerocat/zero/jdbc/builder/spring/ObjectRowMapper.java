package com.github.icezerocat.zero.jdbc.builder.spring;

import com.github.icezerocat.zero.jdbc.builder.annotation.Column;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.NonNull;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * 自定义jdbcTemplate 对象映射器
 *
 * @author zero
 * @date 2021/11/12 18:27
 */
public class ObjectRowMapper<T> extends BeanPropertyRowMapper<T> {

    /**
     * 类字段映射
     */
    private final static Map<Class, Map<String, Field>> CLASS_FIELD_MAP = new ConcurrentHashMap<>();

    /**
     * 缓存注解字段的列名
     */
    private final static Map<Field, String> CACHE_FIELD_COLUMN_NAME = new ConcurrentHashMap<>();

    /**
     * 未定义字段
     */
    private static Field UNDEFINED_FIELD;

    /**
     * 映射类
     */
    private final Class<T> mapperClass;

    static {
        try {
            UNDEFINED_FIELD = ObjectRowMapper.class.getDeclaredField("CLASS_FIELD_MAP");
        } catch (NoSuchFieldException ignored) {
        }
    }

    public ObjectRowMapper(Class<T> mappedClass) {
        super(mappedClass);
        this.mapperClass = mappedClass;
        //初始化类字段
        this.getFieldMap(this.mapperClass);
    }

    /**
     * 将 camelCase 中的名称转换为小写下划线名称
     * 任何大写字母都将转换为带有前下划线的小写字母
     *
     * @param name 原来的名字
     * @return 转换后的名字
     */
    @NonNull
    @Override
    protected String underscoreName(@NonNull String name) {
        Class<T> mappedClass = this.getMappedClass();
        Map<String, Field> fieldMap = this.getFieldMap(mappedClass);
        Field field = fieldMap.get(name);
        if (field == null) {
            field = this.getField(mappedClass, name);
            if (field != null) {
                fieldMap.put(name, field);
                return this.getColumnName(field, mappedClass);
            } else {
                fieldMap.put(name, UNDEFINED_FIELD);
            }
        } else if (field != UNDEFINED_FIELD) {
            return this.getColumnName(field, mappedClass);
        }
        return super.underscoreName(name);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Field field = ObjectMapperUtils.getField(mapperClass, pd.getName());
        Column column = null;
        // begin field
        if (field != null) {
            column = field.getAnnotation(Column.class);
        }
        // if field has not @Column, get from Method.
        if (column == null) {
            Method getterMethod = pd.getReadMethod(), setterMethod = pd.getWriteMethod();
            if ((column = getterMethod.getAnnotation(Column.class)) == null) {
                column = setterMethod.getAnnotation(Column.class);
            }
        }
        // @Column has effective Function.
        if (column != null && Function.class.isAssignableFrom(column.readMapFun())) {
            Object val = JdbcUtils.getResultSetValue(rs, index, Object.class);
            try {
                Function fun = (Function) ObjectMapperUtils.getSingleObject(column.readMapFun());
                return fun.apply(val);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("@Column readMapFun() invoke fail.", e);
            }
        }
        return super.getColumnValue(rs, index, pd);
    }

    /**
     * 获取列名
     *
     * @param field 字段
     * @param clazz 类
     * @return 列名
     */
    private String getColumnName(Field field, Class<?> clazz) {
        String columnName = this.getColumnAnnotationName(field, clazz);
        if (columnName != null) {
            return columnName;
        }
        return super.underscoreName(field.getName());
    }

    /**
     * 获取注解的列名
     *
     * @param field 字段
     * @param clazz 类
     * @return 注解声明的列名
     */
    private String getColumnAnnotationName(Field field, Class<?> clazz) {
        //从缓存获取
        String columnName = CACHE_FIELD_COLUMN_NAME.get(field);
        if (columnName == null) {
            //从字段注解获取
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                String columnValue = column.value().toLowerCase();
                CACHE_FIELD_COLUMN_NAME.put(field, columnValue);
                return columnValue;
            } else {
                //从read、write的注解获取
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, field.getName());
                if (pd != null) {
                    Method readMethod = pd.getReadMethod(), writeMethod = pd.getWriteMethod();
                    if (readMethod != null && (column = readMethod.getAnnotation(Column.class)) != null) {
                        String columnValue = column.value().toLowerCase();
                        CACHE_FIELD_COLUMN_NAME.put(field, columnValue);
                        return column.value();
                    }
                    if (writeMethod != null && (column = writeMethod.getAnnotation(Column.class)) != null) {
                        String columnValue = column.value().toLowerCase();
                        CACHE_FIELD_COLUMN_NAME.put(field, columnValue);
                        return column.value();
                    }
                }
                // save empty string different from null.
                CACHE_FIELD_COLUMN_NAME.put(field, "");
                return null;
            }
        } else if (columnName.length() > 0) {
            return columnName;
        }
        return null;
    }

    /**
     * 根据字段名获取字段，支持超类
     *
     * @param clazz     类
     * @param fieldName 字段名
     * @return 字段
     */
    private Field getField(Class<?> clazz, String fieldName) {
        while (clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 通过类获取映射的字段
     *
     * @param clazz 类
     * @return 需要映射的字段
     */
    private Map<String, Field> getFieldMap(Class<?> clazz) {
        Map<String, Field> fieldMap = CLASS_FIELD_MAP.get(clazz);
        if (fieldMap == null) {
            synchronized (CLASS_FIELD_MAP) {
                fieldMap = CLASS_FIELD_MAP.get(clazz);
                if (fieldMap == null) {
                    fieldMap = new ConcurrentHashMap<>();
                    CLASS_FIELD_MAP.put(clazz, fieldMap);
                }
            }
        }
        return fieldMap;
    }
}
