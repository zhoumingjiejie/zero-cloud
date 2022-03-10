package com.github.icezerocat.zero.jdbc.web.controller;

import com.github.icezerocat.zero.jdbc.builder.Conditions;
import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.entry.Alias;
import com.github.icezerocat.zero.jdbc.builder.entry.Column;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Description: sql构造
 * CreateDate:  2022/1/22 18:06
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
public class SqlBuilderController {

    /**
     * select 操作
     *
     * @return select结果
     */
    public static List select() {
        // 简单查询 "select * from table_a where column1 = 'aaa'"
        String sql1 = SQLBuilder.selectAll().from("table_a").where("column1 = 'aaa'").build();

        // 别名查询 "select column1 as c1, column2 as c2 from table_a as tba"
        String sql2 = SQLBuilder.select(Alias.of("column1", "c1"), Alias.of("column2", "c2")).from(Alias.of("table_a", "tba")).build();

        // 多表条件查询 "select tba.*, tbb.* from table_a as tba, table_b as tbb where tba.column3 = tbb.column3"
        String sql3 = SQLBuilder.select("tba.*", "tbb.*")
                .from(Alias.of("table_a", "tba"), Alias.of("table_b", "tbb"))
                .where("tba.column3", Operator.EQ, "tbb.column3").build();

        // 字段关联
        String sqlc = SQLBuilder.selectAll().from("table_a")
                .where("column1", Operator.EQ, Column.as("column2"))
                .or("column1", Operator.EQ, Column.as("column2"))
                .and("column1", Operator.EQ, Column.as("column2"))
                .build();

        // 条件查询风格, 可根据个人习惯选择风格()
        String sql = SQLBuilder
                .selectAll()
                .from(Alias.of("table_a", "t1"))
                .where("column1 >= 3").and("column2 = 's'") // 方式一: 条件sql编写
                .or("column1", Operator.GE, 3).and("column2", Operator.EQ, "s") // 方式二: 完全参数形式编写
                .or("column1 >= ?", 3).and("column2 = ?", "s") // 方式三: 模板传参形式编写, 模板传参形式值类型传入 "?" 符, 字段名类型传入 "?#" 符
                .build();

        // 使用Supplier增强条件, 防止入参处理中出现异常
        String text = null;
        String sqls = SQLBuilder
                .selectAll()
                .from("table_a")
                .where(text != null, "column1", Operator.IN, () -> text.split(",")) // 若直接填写 text.split(",") 将会抛出空指针异常
                .and(text == null, "column2", Operator.EQ, "text")
                .build();

        // 排序 "select * from table_a order by column1 asc, column2 asc, column3 desc, column4 desc"
        String sql5 = SQLBuilder.selectAll().from("table_a").orderByAsc("column1", "column2").addDesc("column3", "column4").build();

        // 限制数量 "select * from table_a limit 0, 10"
        String sql6 = SQLBuilder.selectAll().from("table_a").limit(0, 10).build();

        // 联表查询 "SELECT t1.*, t2.*, t3.*, t4.* FROM table_a as t1 JOIN table_b as t2 ON t1.column3 = t2.column3 LEFT JOIN table_c as t3 ON t1.column4 = t3.column4 RIGHT JOIN table_d as t4 ON t1.column5 = t4.column5"
        String sql7 = SQLBuilder.select("t1.*", "t2.*", "t3.*", "t4.*")
                .from(Alias.of("table_a", "t1"))
                .join(Alias.of("table_b", "t2"))
                .on("t1.column3", Operator.EQ, Column.as("t2.column3"))
                .leftJoin(Alias.of("table_c", "t3"))
                .on("t1.column4", Operator.EQ, Column.as("t3.column4"))
                .rightJoin(Alias.of("table_d", "t4"))
                .on("t1.column5", Operator.EQ, Column.as("t4.column5"))
                .build();

        // 嵌套查询 "SELECT * FROM (SELECT t1.* FROM table_a JOIN table_b ON table_a.column1 = table_b.column1 LIMIT 100) as t1 WHERE column7 BETWEEN 3 AND 10"
        String sql8 = SQLBuilder.selectAll()
                .from(
                        Alias.of(
                                SQLBuilder.select("table_a.*")
                                        .from("table_a")
                                        .join("table_b")
                                        .on("table_a.column1", Operator.EQ, Column.as("table_b.column1"))
                                        .limit(100)
                                , "t1")
                ).where("column7", Operator.BETWEEN_AND, 3, 10)
                .build();

        // 进阶内容
        // 条件优先级调整查询 "SELECT * FROM table_a, t1 WHERE column1 = 'aa' AND (column2 = 'cc' OR column7 > 10)"
        // 这里若直接连写默认and优先级会高于or, 若要提高or的优先级需要使用使用Conditions工具类生成中间条件
        String sql4 = SQLBuilder.selectAll()
                .from("table_a", "t1")
                .where("column1", Operator.EQ, "aa")
                .and(Conditions.where("column2", Operator.EQ, "cc").or("column7", Operator.GT, 10))
                .build();

        // 动态条件查询, 仅支持条件, 排序(业务场景通常筛选项为filter或sort)
        // "SELECT * FROM table_a as t1 WHERE t1.column3 = 'cc' ORDER BY t1.column7 DESC"
        String sql9 = SQLBuilder.selectAll()
                .from(Alias.of("table_a", "t1"))
                .where(false, "t1.column1 = 'aa'")
                .or(true, "t1.column3", Operator.EQ, "cc")
                .orderByAsc(false, "t1.column5")
                .addDesc(true, "t1.column7")
                .build();

        return Arrays.asList(sql, sql2, sql3, sql4, sql5, sql6, sql7, sql8, sql9);
    }

    public static void main(String[] args) {
        select().forEach(System.out::println);
    }
}
