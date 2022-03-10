package com.github.icezerocat.zero.jdbc.builder.spring.util;

import com.github.icezerocat.zero.jdbc.builder.SQLBuilderException;
import com.github.icezerocat.zero.jdbc.builder.Tuple2;
import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.inner.ObjectMapperUtils;
import com.github.icezerocat.zero.jdbc.builder.spring.annotation.Param;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dragons
 * @date 2021/12/13 9:42
 */
public class SqlUtils {

    private final static String SCRIPT_PREFIX = "#{";

    private final static String SCRIPT_PREFIX2 = "${";

    private final static String SCRIPT_SUFFIX = "}";

    public static Tuple2<String, Object[]> parseSql(String sql, Method method, Object[] args) {
        if (sql == null) return Tuple2.of("", Constants.EMPTY_OBJECT_ARRAY);
        if (args == null || args.length == 0) return Tuple2.of(sql, Constants.EMPTY_OBJECT_ARRAY);
        if (!sql.contains(SCRIPT_PREFIX) && !sql.contains(SCRIPT_PREFIX2) && !sql.contains(SCRIPT_SUFFIX)) return Tuple2.of(sql, args);

        return injectSqlParams(sql, parseParamValue(method, args));
    }

    public static Map<String, Object> parseParamValue(Method method, Object[] args) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<String, Object> valueMap = new HashMap<>();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (args.length > i && args[i] != null) {
                Object value = args[i];
                valueMap.put(SCRIPT_PREFIX + i + SCRIPT_SUFFIX, value);
                if (parameterAnnotations.length > i && parameterAnnotations[i].length > 0) {
                    for (int j = 0; j < parameterAnnotations[i].length; j++) {
                        if (parameterAnnotations[i][j] instanceof Param) {
                            valueMap.put(SCRIPT_PREFIX + ((Param) parameterAnnotations[i][j]).value() + SCRIPT_SUFFIX, value);
                        }
                    }
                }

                if (!ClassUtils.isPrimitiveOrWrapper(parameterTypes[i])) {
                    List<Alias> columnFields = ObjectMapperUtils.getColumnFields(parameterTypes[i]);
                    for (Alias columnField : columnFields) {
                        try {
                            Field field = ObjectMapperUtils.getField(args[i].getClass(), columnField.getAlias());
                            Object fieldValue = ObjectMapperUtils.getFieldValue(args[i], columnField.getAlias());
                            if (field != null && fieldValue != null) {
                                Param p = field.getAnnotation(Param.class);
                                if (p != null) {
                                    valueMap.put(SCRIPT_PREFIX + p.value() + SCRIPT_SUFFIX, fieldValue);
                                } else {
                                    valueMap.put(SCRIPT_PREFIX + columnField.getAlias() + SCRIPT_SUFFIX, fieldValue);
                                }
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            // todo
                            throw new SQLBuilderException("", e);
                        }
                    }
                }
            }
        }
        return valueMap;
    }

    public static Tuple2<String, Object[]> injectSqlParams(String sql, Map<String, Object> valueMap) {
        int stat = 0;
        StringBuilder sqlBuilder = new StringBuilder((int)(sql.length() * 1.5));
        StringBuilder paramBuilder = null;
        List<Object> precompileArgs = new ArrayList<>();
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) == '$' && i + 1 < sql.length() && sql.charAt(i + 1) == '{') {
                stat = 1;
                i++;
                paramBuilder = new StringBuilder();
            } else if (sql.charAt(i) == '#' && i + 1 < sql.length() && sql.charAt(i + 1) == '{') {
                stat = 2;
                i++;
                paramBuilder = new StringBuilder();
            } else if (stat == 1 && sql.charAt(i) == '}') {
                stat = 0;
                sqlBuilder.append(ConditionUtils.parseValue(valueMap.get(SCRIPT_PREFIX + paramBuilder + SCRIPT_SUFFIX)));
            } else if (stat == 2 && sql.charAt(i)  == '}') {
                stat = 0;
                sqlBuilder.append("?");
                precompileArgs.add(valueMap.get(SCRIPT_PREFIX + paramBuilder + SCRIPT_SUFFIX));
            } else if (stat == 1 || stat == 2) {
                paramBuilder.append(sql.charAt(i));
            } else {
                sqlBuilder.append(sql.charAt(i));
            }
        }
        return Tuple2.of(sqlBuilder.toString(), precompileArgs.toArray());
    }
}
