package com.github.icezerocat.zero.jdbc.builder;

import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.util.ConditionUtils;

import java.util.function.Supplier;

/**
 * 条件拼装
 *
 * @author zero
 * @date 2021/11/11 12:38
 */
public class Conditions {

    private Conditions() {
    }

    public static WhereSQLBuilder where(Object... queryCriteria) {
        return where(Boolean.TRUE, queryCriteria);
    }

    public static WhereSQLBuilder where(Boolean predicate, Object... queryCriteria) {
        return new WhereSQLBuilder(predicate, null, queryCriteria);
    }


    public static WhereSQLBuilder where(String condition, Object... params) {
        return where(Boolean.TRUE, condition, params);
    }

    public static WhereSQLBuilder where(Boolean predicate, String condition, Object... params) {
        return new WhereSQLBuilder(predicate, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
    }

    public static WhereSQLBuilder where(Boolean predicate, String condition, Supplier<Object[]> params) {
        return new WhereSQLBuilder(predicate, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
    }


    public static WhereSQLBuilder where(String column, Operator option, Object... params) {
        return where(Boolean.TRUE, column, option, params);
    }

    public static WhereSQLBuilder where(Boolean predicate, String column, Operator option, Object... params) {
        if (predicate) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params);
            return new WhereSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new WhereSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }


    public static WhereSQLBuilder where(Boolean predicate, String column, Operator option, Supplier<Object[]> params) {
        if (predicate) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params.get());
            return new WhereSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new WhereSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }


    public static JoinOnSQLBuilder on(Object... queryCriteria) {
        return on(Boolean.TRUE, queryCriteria);
    }

    public static JoinOnSQLBuilder on(Boolean predicate, Object... queryCriteria) {
        return new JoinOnSQLBuilder(predicate, null, queryCriteria);
    }


    public static JoinOnSQLBuilder on(String condition, Object... params) {
        return on(true, condition, params);
    }

    public static JoinOnSQLBuilder on(Boolean predicate, String condition, Object... params) {
        if (predicate) {
            return new JoinOnSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
        }
        return new JoinOnSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }

    public static JoinOnSQLBuilder on(Boolean predicate, String condition, Supplier<Object[]> params) {
        if (predicate) {
            return new JoinOnSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
        }
        return new JoinOnSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }


    public static JoinOnSQLBuilder on(String column, Operator option, Object... params) {
        return on(true, column, option, params);
    }

    public static JoinOnSQLBuilder on(Boolean predicate, String column, Operator option, Object... params) {
        if (predicate) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params);
            return new JoinOnSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new JoinOnSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }

    public static JoinOnSQLBuilder on(Boolean predicate, String column, Operator option, Supplier<Object[]> params) {
        if (predicate) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params.get());
            return new JoinOnSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new JoinOnSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }


    public static HavingSQLBuilder having(Object... queryCriteria) {
        return having(true, queryCriteria);
    }

    public static HavingSQLBuilder having(Boolean predicate, Object... queryCriteria) {
        return new HavingSQLBuilder(predicate, null, queryCriteria);
    }


    public static HavingSQLBuilder having(String condition, Object... params) {
        return having(true, condition, params);
    }


    public static HavingSQLBuilder having(Boolean predicate, String condition, Object... params) {
        return new HavingSQLBuilder(predicate, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
    }

    public static HavingSQLBuilder having(Boolean predicate, String condition, Supplier<Object[]> params) {
        return new HavingSQLBuilder(predicate, null, Constants.EMPTY_OBJECT_ARRAY, condition, params);
    }


    public static HavingSQLBuilder having(String column, Operator option, Object... params) {
        return having(true, column, option, params);
    }

    public static HavingSQLBuilder having(Boolean predicate, String column, Operator option, Object... params) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params);
            return new HavingSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new HavingSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }

    public static HavingSQLBuilder having(Boolean predicate, String column, Operator option, Supplier<Object[]> params) {
        if (Boolean.TRUE.equals(predicate)) {
            Tuple2<String, Object[]> pt = ConditionUtils.parsePrecompileCondition(column, option, params.get());
            return new HavingSQLBuilder(Boolean.TRUE, null, Constants.EMPTY_OBJECT_ARRAY, pt._1, pt._2);
        }
        return new HavingSQLBuilder(Boolean.FALSE, null, Constants.EMPTY_OBJECT_ARRAY);
    }


}
