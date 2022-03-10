package com.github.icezerocat.zero.jdbc.builder.spring;

import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.SelectSQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.Tuple2;
import com.github.icezerocat.zero.jdbc.builder.WhereSQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.spring.annotation.Delete;
import com.github.icezerocat.zero.jdbc.builder.spring.annotation.Insert;
import com.github.icezerocat.zero.jdbc.builder.spring.annotation.Select;
import com.github.icezerocat.zero.jdbc.builder.spring.annotation.Update;
import com.github.icezerocat.zero.jdbc.builder.spring.util.SqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @author dragons
 * @date 2021/12/10 18:07
 */
public class JdbcBaseMapperHandler {

    private final static Logger log = LoggerFactory.getLogger(JdbcBaseMapperHandler.class);

    private final Class beanClass;

    private final JdbcTemplate jdbcTemplate;

    private final BeanPropertyRowMapper beanMapper;

    private final String prefixSql;

    public JdbcBaseMapperHandler(Class beanClass, JdbcTemplate jdbcTemplate, BeanPropertyRowMapper beanMapper) {
        this.beanClass = beanClass;
        this.jdbcTemplate = jdbcTemplate;
        this.beanMapper = beanMapper;
        this.prefixSql = SQLBuilder.select(beanClass).from(beanClass).build();
    }

    public Object handle(Method method, Object[] args) {
       Object result = handleAnnotation(method, args);
       if (result == null) {
           result = handleMethod(method, args);
       }
       return result;
    }

    private Object handleMethod(Method method, Object[] args) {
        String methodLowerCaseName = method.getName().toLowerCase();
        if (methodLowerCaseName.startsWith("select")) {
            return handleSelectMethod(method, args);
        } else if (methodLowerCaseName.startsWith("insert")) {
            return handleInsertMethod(method, args);
        } else if (methodLowerCaseName.startsWith("update")) {
            return handleUpdateMethod(method, args);
        } else if (methodLowerCaseName.startsWith("delete")) {
            return handleDeleteMethod(method, args);
        }
        return null;
    }

    private Object handleDeleteMethod(Method method, Object[] args) {
        if (args == null || args.length == 0) throw new RuntimeException("There must be at least one delete method parameter.");
        return handleDmlSql(method, SQLBuilder.delete(beanClass).where((WhereSQLBuilder) args[0]).build());
    }

    private Object handleUpdateMethod(Method method, Object[] args) {
        if (args == null || args.length == 0) throw new RuntimeException("There must be at least one update method parameter.");
        WhereSQLBuilder sqlBuilder = SQLBuilder.update(args[0]);
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                if  (args[i] instanceof WhereSQLBuilder) {
                    sqlBuilder = sqlBuilder.and((WhereSQLBuilder) args[i]);
                }
            }
        }
        return handleDmlSql(method, sqlBuilder.build());
    }

    private Object handleInsertMethod(Method method, Object[] args) {
        if (args == null || args.length == 0) throw new RuntimeException("There must be at least one insert method parameter.");
        return handleDmlSql(method, SQLBuilder.insertInto(args).build());
    }

    private Object handleSelectMethod(Method method, Object[] args) {
        SQLBuilder extensionSQLBuilder = null;
        SelectSQLBuilder selectSqlBuilder = null;
        if (args != null && args.length > 0) {
            for(Object arg : args) {
                if (arg instanceof SelectSQLBuilder) {
                    selectSqlBuilder = (SelectSQLBuilder) arg;
                }
                else if (arg instanceof SQLBuilder && extensionSQLBuilder == null) {
                    extensionSQLBuilder = (SQLBuilder) arg;
                }
            }
        }
        String sql;
        Object[] precompileArgs = Constants.EMPTY_OBJECT_ARRAY;
        if (extensionSQLBuilder != null) {
            String sqlSuffix = extensionSQLBuilder.precompileSQL();
            precompileArgs = extensionSQLBuilder.precompileArgs();
            if (selectSqlBuilder != null) {
                sql = selectSqlBuilder.from(beanClass) + sqlSuffix;
            } else {
                sql = prefixSql + sqlSuffix;
            }
        } else {
            sql = prefixSql;
        }
        return handleSelectSql(method,  sql, precompileArgs);
    }

    private Object handleAnnotation(Method method, Object[] args) {
        Map<Type, String> annotationMap = parseAnnotation(method);
        if (annotationMap != null) {
            String sql;
            if ((sql = annotationMap.get(Type.DML)) != null) {
                return jdbcTemplate.update(sql);
            } else if ((sql = annotationMap.get(Type.DQL)) != null) {
                // parse
                Tuple2<String, Object[]> pt = SqlUtils.parseSql(sql, method, args);
                return handleSelectSql(method, pt._1, pt._2);
            }
        }
        return null;
    }

    private Object handleDmlSql(Method method, String ...sql) {
        Class returnType = method.getReturnType();

        log.debug("Execute sql: " + sql[0]);
        int[] u = jdbcTemplate.batchUpdate(sql);
        if (u.length == 0) u = new int[]{0};
        if (returnType == Long.class) {
            return (long) u[0];
        } else if (returnType == Float.class) {
            return (float) u[0];
        } else if (returnType == Double.class) {
            return (double) u[0];
        } else if (returnType == BigInteger.class) {
            return BigInteger.valueOf(u[0]);
        } else if (returnType == BigDecimal.class) {
            return BigDecimal.valueOf(u[0]);
        }
        return u;
    }

    private Object handleSelectSql(Method method, String sql, Object[] args) {
        Class returnType = method.getReturnType();

        if (log.isDebugEnabled()) {
            log.debug("Execute sql: " + sql);
        }
        Object result;
        if (returnType == Integer.class || returnType == Long.class) {
            result = jdbcTemplate.queryForObject(sql, args, returnType);
        } else {
            result = jdbcTemplate.query(sql, (ps) -> {
                if (args != null && args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        ps.setObject(i + 1, args[i]);
                    }
                }
            }, beanMapper);
        }

        if (returnType == Integer.class || returnType == Long.class) {
            return result;
        }

        List items = (List) result;
        if (beanClass.isAssignableFrom(returnType)) {
            if (!items.isEmpty()) {
                return items.get(0);
            } else {
                throw new RuntimeException("Return type \"" + returnType.getName() +  "\" but result is empty.");
            }
        } else if (returnType == List.class) {
            return items;
        } else if (returnType == Set.class) {
            return new HashSet(items);
        }
        return null;
    }

    private Map<Type, String> parseAnnotation(Method method) {
        Insert insert = method.getAnnotation(Insert.class);
        Update update = method.getAnnotation(Update.class);
        Delete delete = method.getAnnotation(Delete.class);
        Select select = method.getAnnotation(Select.class);
        if (insert != null) {
            Map<Type, String> m = new HashMap<>();
            m.put(Type.DML, insert.value());
            return m;
        }
        if (update != null) {
            Map<Type, String> m = new HashMap<>();
            m.put(Type.DML, update.value());
            return m;
        }
        if (delete != null) {
            Map<Type, String> m = new HashMap<>();
            m.put(Type.DML, delete.value());
            return m;
        }
        if (select != null) {
            Map<Type, String> m = new HashMap<>();
            m.put(Type.DQL, select.value());
            return m;
        }
        return null;
    }

    enum Type{
        DML,
        DQL,
    }
}
