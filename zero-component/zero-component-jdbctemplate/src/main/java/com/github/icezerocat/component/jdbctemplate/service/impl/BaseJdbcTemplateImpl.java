package com.github.icezerocat.component.jdbctemplate.service.impl;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.github.icezerocat.component.jdbctemplate.annotations.TableField;
import com.github.icezerocat.component.jdbctemplate.annotations.TableId;
import com.github.icezerocat.component.jdbctemplate.builder.SearchBuild;
import com.github.icezerocat.component.jdbctemplate.service.BaseJdbcTemplate;
import com.github.icezerocat.component.jdbctemplate.utils.DaoUtil;
import com.github.icezerocat.component.jdbctemplate.utils.StringUtil;
import com.github.icezerocat.zerocommon.model.Param;
import com.github.icezerocat.zerocommon.utils.ReflectAsmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * ProjectName: [framework]
 * Package:     [github.com.icezerocat.admin.jdbcTemplate.Impl.BaseJdbcTemplate]
 * Description: jdbcTemplate扩展类
 * CreateDate:  2020/4/4 18:54
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@Component
public class BaseJdbcTemplateImpl extends JdbcTemplate implements BaseJdbcTemplate {
    @SuppressWarnings("all")
    public BaseJdbcTemplateImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public long delete(Class<?> tClass, @NotNull Long id) {
        String sql = "delete from " + DaoUtil.getTableName(tClass) + " where id = ?";
        return this.update(sql, id);
    }

    @Override
    public long delete(Class<?> tClass, Iterable<Long> ids) {
        String id = String.valueOf(ids);
        id = id.substring(1, id.length() - 1);
        String sql = "delete from " + DaoUtil.getTableName(tClass) + " where id in (" + id + ")";
        return this.update(sql, id);
    }

    @Override
    public <T> T findById(Class<T> tClass, @NotNull Long id) {
        return this.queryForObject("select * from " + DaoUtil.getTableName(tClass) + " where id = " + id, new BeanPropertyRowMapper<>(tClass));
    }

    @Override
    public <T> List<T> findById(Class<T> tClass, Iterable<Long> ids) {
        String id = String.valueOf(ids);
        id = id.substring(1, id.length() - 1);
        return this.query("select * from " + DaoUtil.getTableName(tClass) + " where id in (" + id + ")", new BeanPropertyRowMapper<>(tClass));
    }

    @Override
    public <T> List<T> findAll(String sqlStr, Object[] objects, Class<T> entityClass, Pageable pageable) {
        return executePageable(sqlStr, objects, entityClass, pageable);
    }

    @Override
    public <T> List<T> findAll(String sqlStr, Object[] objects, Class<T> tClass) {
        return this.query(sqlStr, objects, new BeanPropertyRowMapper<>(tClass));
    }

    @Override
    public <T> List<T> findAll(Class<T> tClass) {
        return this.query("select * from " + DaoUtil.getTableName(tClass), new Object[]{}, new BeanPropertyRowMapper<>(tClass));
    }

    @Override
    public long count(Class tClass) {
        Long count = this.queryForObject("select count(*) from " + DaoUtil.getTableName(tClass), new Object[]{}, Long.class);
        return count == null ? 0L : count;
    }

    @Override
    public long count(List<Param> searchList, Class<?> entityClass) {
        SearchBuild searchBuild = new SearchBuild.Builder(DaoUtil.getTableName(entityClass)).searchList(searchList).start();
        String sql = " select count(*) " + searchBuild.getHql();
        Long count = this.queryForObject(sql, searchBuild.getList(), Long.class);
        return count == null ? 0L : count;
    }

    @Override
    public <T> List<T> getList(List<Param> searchList, Class<T> entityClass, Pageable pageable) {
        SearchBuild searchBuild = new SearchBuild.Builder(DaoUtil.getTableName(entityClass)).searchList(searchList).start();
        String sql = "SELECT * " + searchBuild.getHql();
        return executePageable(sql, searchBuild.getList(), entityClass, pageable);
    }

    @Override
    public int[] insert(Class<?> tClass) {
        List<Object[]> list = new ArrayList<>();
        list.add(getInsertValue(tClass));
        return this.batchUpdate(getInsertSql(tClass), list);
    }

    @Override
    public <T> int[] insert(List<T> listT) {
        List<Object[]> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(listT)) {
            return new int[]{-2};
        }
        listT.forEach(t -> list.add(getInsertValue(t)));
        return this.batchUpdate(getInsertSql(listT.get(0).getClass()), list);
    }

    @Override
    public  <T> Object[] getInsertValue(T t) {
        Class tClass = t.getClass();
        List<Object> objectList = new ArrayList<>();
        MethodAccess methodAccess = ReflectAsmUtil.get(tClass);
        Field[] fields = tClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            //忽略static、Ignore、Transient、id
            String fieldName = field.getName();
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            TableField tableField = field.getAnnotation(TableField.class);
            boolean existBl = tableField == null || tableField.exist();
            boolean id = Objects.equals("ID", fieldName.toUpperCase());
            if (!isStatic && existBl && !id) {
                //获取方法名
                String methodName;
                if (field.getType() == boolean.class) {
                    methodName = fieldName.contains("is") ? fieldName : "is" + StringUtils.capitalize(fieldName);
                } else {
                    methodName = "get" + StringUtils.capitalize(fieldName);
                }
                objectList.add(methodAccess.invoke(t, methodName));
            }
        }
        return objectList.toArray();
    }

    @Override
    public String getInsertSql(Class<?> tClass) {
        StringBuilder sql = new StringBuilder().append(" INSERT INTO ").append(DaoUtil.getTableName(tClass)).append(" ( ");
        StringBuilder o = new StringBuilder();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //忽略static、Ignore、Transient、id
            String fieldName = field.getName();
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            TableField tableField = field.getAnnotation(TableField.class);
            boolean existBl = tableField == null || tableField.exist();
            boolean id = Objects.equals("ID", fieldName.toUpperCase());
            if (!isStatic && existBl && !id) {
                if (tableField != null && org.apache.commons.lang3.StringUtils.isNotBlank(tableField.value())) {
                    fieldName = tableField.value();
                } else {
                    fieldName = StringUtil.camel2Underline(fieldName);
                }
                sql.append(fieldName).append(" , ");
                o.append(" ? ").append(" , ");
            }
        }
        sql.delete(sql.length() - 2, sql.length());
        o.delete(o.length() - 2, o.length());
        sql.append(" ) ").append(" VALUES ").append(" ( ").append(o.toString()).append(" ) ");
        return sql.toString();
    }

    /**
     * 解析pageable后执行sql
     *
     * @param sqlStr      sql语句
     * @param objects     查询值
     * @param entityClass 返回对象
     * @param pageable    分页排序
     * @return 结果集
     */
    private <T> List<T> executePageable(String sqlStr, Object[] objects, Class<T> entityClass, Pageable pageable) {

        StringBuilder sqlBuild = new StringBuilder(sqlStr);

        //是否有select，没有则添加
        final String select = "SELECT";
        if (!sqlStr.trim().toUpperCase().startsWith(select)) {
            sqlBuild.insert(0, " SELECT ");
        }

        //排序语句拼接
        if (pageable != null && !pageable.getSort().isEmpty()) {
            sqlBuild.append(" ORDER BY ");
            String sortStr = pageable.getSort().toString();
            String[] sortArr = sortStr.split(",");
            for (String sort : sortArr) {
                String[] sortObj = sort.split(":");
                sqlBuild.append(sortObj[0]).append(" ").append(sortObj[1]).append(",");
            }
            sqlBuild.replace(sqlBuild.length() - 1, sqlBuild.length(), " ");
        }

        List<Object> list = new ArrayList<>(Arrays.asList(objects));
        //分页语句拼接(分页大小等于1，取全部数据)
        if (pageable != null && pageable.getPageSize() != 1) {
            sqlBuild.append(" LIMIT ?,? ");
            list.add((pageable.getPageNumber() - 1) * pageable.getPageSize());
            list.add(pageable.getPageSize());
        }

        return this.query(sqlBuild.toString(), list.toArray(), new BeanPropertyRowMapper<>(entityClass));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> int saveOrUpdateBatch(Collection<T> tCollection) {
        List<T> insertList = new ArrayList<T>();
        List<T> updateList = new ArrayList<T>();
        List<Object[]> updateValueList = new ArrayList<>();
        TableCheck tableCheck = null;
        for (T t : tCollection) {
            tableCheck = this.containsKey(t.getClass(), t);
            if (!tableCheck.isContainsKey()) {
                insertList.add(t);
            } else {
                updateValueList.add(this.getUpdateValue(t, tableCheck));
                updateList.add(t);
            }
        }

        //操作总数
        int count = 0;
        //插入总数
        if (!CollectionUtils.isEmpty(insertList)) {
            int[] insert = this.insert(insertList);
            int insertCount = insert != null && insert.length > 0 ? insert.length : 0;
            count += insertCount;
        }

        //更新总数
        if (!CollectionUtils.isEmpty(updateValueList)) {
            int[] batchUpdate = this.batchUpdate(this.getUpdateSql(updateList.get(0).getClass(), tableCheck), updateValueList);
            int updateCount = Math.max(batchUpdate.length, 0);
            count += updateCount;
        }

        return count;
    }

    @Override
    public  <T> Object[] getUpdateValue(T t, TableCheck tableCheck) {
        Class tClass = t.getClass();
        List<Object> objectList = new ArrayList<>();
        MethodAccess methodAccess = ReflectAsmUtil.get(tClass);
        Field[] fields = tClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            //忽略static、Ignore、Transient、id
            String fieldName = field.getName();
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            TableField tableField = field.getAnnotation(TableField.class);
            boolean existBl = tableField == null || tableField.exist();
            boolean isPrimaryKey = false;
            for (String key : tableCheck.getIdsMap().keySet()) {
                if (key.toUpperCase().equals(fieldName.toUpperCase())) {
                    isPrimaryKey = true;
                    break;
                }
            }
            if (!isStatic && existBl && !isPrimaryKey) {
                //获取方法名
                String methodName;
                if (field.getType() == boolean.class) {
                    methodName = fieldName.contains("is") ? fieldName : "is" + StringUtils.capitalize(fieldName);
                } else {
                    methodName = "get" + StringUtils.capitalize(fieldName);
                }
                objectList.add(methodAccess.invoke(t, methodName));
            }
        }
        //添加主键搜索条件ID字段
        for (Map.Entry<String, Object> map : tableCheck.getIdsMap().entrySet()) {
            objectList.add(map.getValue());
        }
        return objectList.toArray();
    }

    @Override
    public  <T> String getUpdateSql(Class<T> tClass, TableCheck tableCheck) {
        StringBuilder sql = new StringBuilder().append(" UPDATE ").append(DaoUtil.getTableName(tClass)).append(" SET ");
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //忽略static、Ignore、Transient、id
            String fieldName = field.getName();
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            TableField tableField = field.getAnnotation(TableField.class);
            boolean existBl = tableField == null || tableField.exist();
            boolean isPrimaryKey = false;
            for (String key : tableCheck.getIdsMap().keySet()) {
                if (key.toUpperCase().equals(fieldName.toUpperCase())) {
                    isPrimaryKey = true;
                    break;
                }
            }
            if (!isStatic && existBl && !isPrimaryKey) {
                sql.append(StringUtil.camel2Underline(fieldName)).append(" = ? ");
                sql.append(",");
            }
        }
        //去除逗号
        sql.delete(sql.length() - 1, sql.length());
        int index = 0;
        for (String key : tableCheck.getIdsMap().keySet()) {
            if (index == 0) {
                sql.append(" WHERE ").append(key).append(" = ").append(" ? ");
            } else {
                sql.append(" AND ").append(key).append(" = ").append(" ? ");
            }
            index++;
        }

        return sql.toString();
    }

    @Override
    public TableCheck containsKey(Class<?> cls, Object entity) {
        TableCheck tableCheck = new TableCheck();
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            TableId tableIdAnn = field.getAnnotation(TableId.class);
            if (tableIdAnn != null) {
                String name = org.apache.commons.lang3.StringUtils.isNotBlank(tableIdAnn.value()) ?
                        tableIdAnn.value() : StringUtil.camel2Underline(field.getName());
                Object id = null;
                try {
                    id = field.get(entity);
                } catch (IllegalAccessException e) {
                    log.error("The primary key parsing errors：{}", e.getMessage());
                    e.printStackTrace();
                }
                boolean exitsBl = tableCheck.isContainsKey() && (id == null || org.apache.commons.lang3.StringUtils.isBlank(String.valueOf(id)));
                if (exitsBl) {
                    tableCheck.setContainsKey(false);
                }
                tableCheck.getIdsMap().put(name, id);
            }
        }
        tableCheck.setKeyCount(tableCheck.getIdsMap().size());
        return tableCheck;
    }

    /**
     * 表单检查类
     */
    public static class TableCheck implements Serializable {

        public TableCheck() {
        }

        /**
         * 更具主键判断数据是否存在（true：存在）
         */
        private boolean containsKey = true;

        /**
         * 主键总数
         */
        private int keyCount;

        /**
         * 主键字段
         */
        private Map<String, Object> idsMap = new HashMap<>();

        public boolean isContainsKey() {
            return containsKey;
        }

        public void setContainsKey(boolean containsKey) {
            this.containsKey = containsKey;
        }

        public int getKeyCount() {
            return keyCount;
        }

        public void setKeyCount(int keyCount) {
            this.keyCount = keyCount;
        }

        public Map<String, Object> getIdsMap() {
            return idsMap;
        }

        public void setIdsMap(Map<String, Object> idsMap) {
            this.idsMap = idsMap;
        }

        @Override
        public String toString() {
            return "TableCheck{" +
                    "containsKey=" + containsKey +
                    ", keyCount=" + keyCount +
                    ", idsMap=" + idsMap +
                    '}';
        }
    }
}
