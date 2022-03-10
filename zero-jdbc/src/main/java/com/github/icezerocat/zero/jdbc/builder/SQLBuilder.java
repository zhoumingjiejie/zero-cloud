package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.InsertMode;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sql Builder top interface and a bootstrap.
 *
 * @author dragons
 * @date 2021/11/9 18:28
 */
public interface SQLBuilder extends PreparedStatementSupport {

    Logger log = LoggerFactory.getLogger(SQLBuilder.class);

    /**
     * Core method. It defines how to build a sql or sql fragment.
     *
     * @return Sql or sql fragment.
     */
    default String build() {
        return ConditionUtils.parseTemplate(precompileSQL(), precompileArgs());
    }

    /**
     * 选择所有列
     *
     * @return A SelectSQLBuilder instance.
     */
    static SelectSQLBuilder selectAll() {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, "*");
    }

    /**
     * 选择指定列
     *
     * @return A SelectSQLBuilder instance.
     */
    static SelectSQLBuilder select(String... columns) {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, columns);
    }

    /**
     * 选择返回列
     *
     * @param columns 别名列
     * @return A SelectSQLBuilder instance.
     */
    static SelectSQLBuilder select(Alias... columns) {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, columns);
    }

    /**
     * 选择指定列
     *
     * @param columns 列名：CharSequence、Alias、Class格式
     * @return A SelectSQLBuilder instance.
     */
    static SelectSQLBuilder select(Object... columns) {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, columns);
    }

    /**
     * Single table merge select and from
     *
     * @return A FromSQLBuilder instance
     */
    static FromSQLBuilder model(Class<?> modelClass) {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, modelClass).from(modelClass);
    }

    /**
     * 对象单表合并 select and from存在别名操作
     *
     * @return A FromSQLBuilder instance
     */
    static FromSQLBuilder model(String modelClass) {
        return new SelectSQLBuilder(null, Constants.EMPTY_OBJECT_ARRAY, modelClass).from(modelClass);
    }

    /**
     * @return A InsertSQLBuilder instance.
     */
    static InsertSQLBuilder insert(InsertMode mode, String table, String... columns) {
        return new InsertSQLBuilder(table, mode, columns);
    }

    static <T> ValuesSQLBuilder insert(InsertMode mode, T... models) {
        return insert(mode, Arrays.asList(models));
    }

    static <T> ValuesSQLBuilder insert(InsertMode mode, List<T> models) {
        if (models == null || models.isEmpty()) {
            throw new SQLBuilderException("The inserted model cannot be null.");
        }
        Class clazz = models.get(0).getClass();
        String tableName = ObjectMapperUtils.getTableName(clazz);
        List<Alias> fields = ObjectMapperUtils.getColumnFields(clazz);
        InsertSQLBuilder builder = insert(mode, tableName, fields.stream().map(Alias::getOrigin).toArray(String[]::new));
        return builder.values().addValues(
                models.stream().map(e -> {
                    Object[] values = new Object[fields.size()];
                    for (int i = 0; i < fields.size(); i++) {
                        try {
                            values[i] = ObjectMapperUtils.getFieldValue(e, fields.get(i).getAlias());
                        } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                            values[i] = null;
                            log.warn("The value of the inserted model field " + fields.get(i).getAlias() + " is not getting properly and is ignored.");
                        }
                    }
                    return values;
                }).collect(Collectors.toList())
        );
    }

    /**
     * @return A InsertSQLBuilder instance.
     */
    static InsertSQLBuilder insertInto(String table, String... columns) {
        return new InsertSQLBuilder(table, InsertMode.INSERT_INTO, columns);
    }

    static <T> ValuesSQLBuilder insertInto(T... models) {
        return insert(InsertMode.INSERT_INTO, models);
    }

    static <T> ValuesSQLBuilder insertInto(List<T> models) {
        return insert(InsertMode.INSERT_INTO, models);
    }

    /**
     * @return A InsertSQLBuilder instance.
     */
    static InsertSQLBuilder insertIgnore(String table, String... columns) {
        return new InsertSQLBuilder(table, InsertMode.INSERT_IGNORE, columns);
    }

    static <T> ValuesSQLBuilder insertIgnore(T... models) {
        return insert(InsertMode.INSERT_IGNORE, models);
    }

    static <T> ValuesSQLBuilder insertIgnore(List<T> models) {
        return insert(InsertMode.INSERT_IGNORE, models);
    }

    /**
     * @return A InsertSQLBuilder instance.
     */
    static InsertSQLBuilder insertOverwrite(String table, String... columns) {
        return new InsertSQLBuilder(table, InsertMode.INSERT_OVERWRITE, columns);
    }

    static <T> ValuesSQLBuilder insertOverwrite(T... models) {
        return insert(InsertMode.INSERT_OVERWRITE, models);
    }

    static <T> ValuesSQLBuilder insertOverwrite(List<T> models) {
        return insert(InsertMode.INSERT_OVERWRITE, models);
    }

    /**
     * @return A InsertSQLBuilder instance.
     */
    static InsertSQLBuilder replaceInto(String table, String... columns) {
        return new InsertSQLBuilder(table, InsertMode.REPLACE_INTO, columns);
    }

    static <T> ValuesSQLBuilder replaceInto(T... models) {
        return insert(InsertMode.REPLACE_INTO, models);
    }

    static <T> ValuesSQLBuilder replaceInto(List<T> models) {
        return insert(InsertMode.REPLACE_INTO, models);
    }

    /**
     * @return A UpdateSQLBuilder instance
     */
    static UpdateSQLBuilder update(String table) {
        return new UpdateSQLBuilder(table);
    }

    static <T> WhereSQLBuilder update(T model) {
        Class<?> clazz = model.getClass();
        String tableName = ObjectMapperUtils.getTableName(clazz);
        List<Alias> fields = ObjectMapperUtils.getColumnFields(clazz);
        List<Alias> primaries = ObjectMapperUtils.getPrimaries(clazz);
        Map<String, Object> primaryMapping;
        try {
            if (primaries == null || primaries.isEmpty()) {
                primaryMapping = Collections.emptyMap();
            } else {
                primaryMapping = ObjectMapperUtils.getColumnAndValues(model, primaries);
                fields.removeAll(primaries);
            }
            Map<String, Object> updateMapping = ObjectMapperUtils.getColumnAndValues(model, fields);

            if (updateMapping.isEmpty()) {
                throw new SQLBuilderException("At least one value of the updated field cannot be null.");
            }

            UpdateSQLBuilder updateSqlBuilder = update(tableName);
            SetSQLBuilder setSqlBuilder = null;
            for (Map.Entry<String, Object> entry : updateMapping.entrySet()) {
                if (setSqlBuilder == null) {
                    setSqlBuilder = updateSqlBuilder.set(entry.getKey(), entry.getValue());
                } else {
                    setSqlBuilder = setSqlBuilder.addSet(entry.getKey(), entry.getValue());
                }
            }

            WhereSQLBuilder whereSqlBuilder = null;
            if (!primaryMapping.isEmpty()) {
                for (Map.Entry<String, Object> entry : primaryMapping.entrySet()) {
                    if (whereSqlBuilder == null) {
                        whereSqlBuilder = setSqlBuilder.where(entry.getKey(), Operator.EQ, entry.getValue());
                    } else {
                        whereSqlBuilder = whereSqlBuilder.and(entry.getKey(), Operator.EQ, entry.getValue());
                    }
                }
            } else {
                whereSqlBuilder = setSqlBuilder.where("");
            }
            return whereSqlBuilder;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SQLBuilderException("Obtaining the model value is abnormal", e);
        }
    }

    /**
     * @return A DeleteSQLBuilder instance
     */
    static DeleteSQLBuilder delete(String table) {
        return new DeleteSQLBuilder(table);
    }

    static <T> DeleteSQLBuilder delete(T model) {
        Class<?> clazz = model.getClass();
        return new DeleteSQLBuilder(ObjectMapperUtils.getTableName(clazz));
    }
}
