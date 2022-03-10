###
## SqlBuilder 一个简单易用的Sql建造器
###




### 3分钟上手教程

##### 构建 DQL sql

```java
import club.kingon.sql.builder.SQLBuilder;
import club.kingon.sql.builder.SqlBuilder;
import club.kingon.sql.builder.entry.Alias;
import club.kingon.sql.builder.entry.Column;
import club.kingon.sql.builder.enums.Operator;

class Example {
    public static void main(String[] args) {
        // 简单查询 "select * from table_a where column1 = 'aaa'"
        String sql1 = SQLBuilder.selectAll().from("table_a").where("column1 = 'aaa'").build();

        // 别名查询 "select column1 as c1, column2 as c2 from table_a as tba"
        String sql2 = SQLBuilder.select(Alias.of("column1", "c1"), Alias.of("column2", "c2")).from(Alias.of("table_a", "tba")).build();

        // 多表条件查询 "select tba.*, tbb.* from table_a as tba, table_b as tbb where tba.column3 = tbb.column3"
        String sql3 = SQLBuilder.select("tba.*", "tbb.*").from(Alias.of("table_a", "tba"), Alias.of("table_b", "tbb")).whereColumn("tba.column3", Operator.EQ, "tbb.column3").build();

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
    }
}
```

##### 2022-01-20 新增预编译SQL支持
```java
class Example {
    public static void main(String[] args) {
        SQLBuilder builder = SQLBuilder
            .select("t1.*", "t2.*")
            .from("t1")
            .join("t2")
            .on("t1.a = t2.a")
            .where("t1.b", Operator.GE, 10)
            .or("t2.b", Operator.LE, 5)
            .or(Conditions.where("t1.c", Operator.IN, 3, 4, 5).and("t2.c", Operator.BETWEEN_AND, 5, 10))
            .and("t1.b", Operator.LRLIKE, 1)
            .groupBy("t1.z")
            .having("count(1)", Operator.GE, 100)
            .orderBy("t1.z", Order.ASC)
            .limit(10, 100);
        // 获取完整SQL 
        // SELECT t1.*, t2.* FROM t1 JOIN t2 ON t1.a = t2.a WHERE (t1.b >= 10 OR t2.b <= 5 OR (t1.c IN (3, 4, 5) AND t2.c BETWEEN 5 AND 10)) AND t1.b LIKE '%1%' GROUP BY t1.z HAVING count(1) >= 100 ORDER BY t1.z ASC LIMIT 10, 100
        System.out.println(builder.build());
        // 获取预编译SQL
        // SELECT t1.*, t2.* FROM t1 JOIN t2 ON t1.a = t2.a WHERE (t1.b >= ? OR t2.b <= ? OR (t1.c IN (?, ?, ?) AND t2.c BETWEEN ? AND ?)) AND t1.b LIKE ? GROUP BY t1.z HAVING count(1) >= ? ORDER BY t1.z ASC LIMIT ?, ?
        System.out.println(builder.precompileSQL());
        // 获取预编译SQL参数
        // [10, 5, 3, 4, 5, 5, 10, %1%, 100, 10, 100]
        System.out.println(Arrays.toString(builder.precompileArgs()));
    }
}
```

##### 2021-12-09 新增Model支持

```java

import club.kingon.sql.builder.SQLBuilder;
import club.kingon.sql.builder.annotation.Column;
import club.kingon.sql.builder.annotation.Primary;
import club.kingon.sql.builder.annotation.Query;
import club.kingon.sql.builder.enums.Operator;

import java.util.Arrays;

class Example {
    public static void main(String[] args) {
        System.out.println(
            // SQLBuilder.model(Class) => SQLBuilder.select(...).from(...)
            // 查询商品表中价格大于等于10，且状态为1,2,3的商品名称带有手机的商品
            // select id, goods_name, status from goods_tb where price >= 10 and goods_name like '%手机%' and status in (1, 2, 3)
            SQLBuilder.model(Goods.class)
                .where("price >= 10")
                .and(new GoodsCriteria(null, "手机", Arrays.asList(1, 2, 3)))
                .build()
        );
    }
}

@Table("goods_tb")
class Goods {
    @Primary
    @Column("goods_id")
    private Integer id;
    /**
     * 默认映射 goods_name
     */
    private String goodsName;

    private Integer status;
}

class GoodsCriteria {
    /**
     * goods_id 精确查询
     */
    @Query(value = "goods_id", type = Operator.EQ)
    private Integer goodsIdEq;
    /**
     * goods_name 模糊查询
     */
    @Query(value = "goods_name", type = Operator.LRLIKE)
    private String goodsNameLRLike;
    /**
     * in 查询
     */
    @Query(value = "status", type = Operator.IN)
    private List<Integer> statusIn;

    public GoodsCriteria(Integer goodsIdEq, String goodsNameLRLike, List<Integer> statusIn) {
        this.goodsIdEq = goodsIdEq;
        this.goodsNameLRLike = goodsNameLRLike;
        this.statusIn = statusIn;
    }
}
```

##### 构建 DML sql

```java
import club.kingon.sql.builder.SQLBuilder;
import club.kingon.sql.builder.SqlBuilder;
import club.kingon.sql.builder.annotation.Primary;
import club.kingon.sql.builder.enums.Operator;

@Table("good")
class Goods {
    @Primary
    @Column("goods_id")
    private Integer id;

    @Column("goods_name")
    private String name;

    private Long price;

    /**
     * 映射 first_category字段
     */
    private String firstCategory;

    public Goods() {
    }

    public Goods(String name, Long price, String firstCategory) {
        this.name = name;
        this.price = price;
        this.firstCategory = firstCategory;
    }
}

class Example {
    public static void main(String[] args) {
        // 插入
        // 默认全部字段插入 "insert into table_a values('c1', 'c2'), ('cc1', 'cc2')"
        String sql1 = SQLBuilder.insertInto("table_a").values().addValue("c1", "c2").addValue("cc1", "cc2").build();

        // 指定插入字段 "insert into table_a(column1, column2) values('c1', 'c2'), ('cc1', 'cc2')"
        String sql2 = SQLBuilder.insertInto("table_a", "column1", "column2").values().addValue("cc1", "cc2").addValue("c1", "c2").build();

        // 指定插入字段 "insert ignore table_a(column1, column2) values('c1', 'c2'), ('cc1', 'cc2')"
        String sql3 = SQLBuilder.insertIgnore("table_a", "column1", "column2").values().addValue("cc1", "cc2").addValue("c1", "c2").build();

        // 指定插入字段 "replace into table_a(column1, column2) values('c1', 'c2'), ('cc1', 'cc2')"
        String sql4 = SQLBuilder.replaceInto("table_a", "column1", "column2").values().addValue("cc1", "cc2").addValue("c1", "c2").build();

        // 2021-12-07 更新, 支持model插入字段 "INSERT INTO good(price, goods_name, first_category, goods_id) VALUES(2300, 'name1', '手机', null)"
        String sqlm1 = SQLBuilder.insertInto(new Goods("name1", 2300L, "手机"))
            // 支持duplicate
//            .onDuplicateKeyUpdateColumn("goods_name")
            .build();

        // 修改
        // 纯sql条件 "update table_a set column1 = '3' where column2 = 'cc1'"
        String sql5 = SQLBuilder.update("table_a").set("column1 = '3'").where("column2 = 'cc1'").build();

        // 参数sql条件 "update table_a set column1 = '3' where column2 = 'cc1'" 
        String sql6 = SQLBuilder.update("table_a").set("column1", "3").where("column2", Operator.EQ, "cc1").build();

        // update from 语法 update table_a set table_a.column3 = table_b.column3 from table_b where table_a.column1 = table_b.column1
        String sql7 = SQLBuilder.update("table_a").setColumn("table_a.column3", "table_b.column3")
            .from("table_b").whereColumn("table_a.column1", Operator.EQ, "table_b.column1").build();

        // 2021-12-07 更新, 支持model更新字段 "UPDATE good SET goods_name = 'name1' WHERE goods_id = 1"
        // 主键id不为null则以主键id为基准查询，否则需自行添加额外判断条件
        String sqlm2 = SQLBuilder.update(new Goods(1, "name1", null, null))
            // 支持多条件查询
//            .and(...)
            .build();

        // 删除
        // "delete from table_a where column4 in ('a', 'b', 'c', 'd')"
        String sql8 = SQLBuilder.delete("table_a").where("column4", Operator.IN, "a", "b", "c", "d").build();
    }
}
```

### 集成 Spring JdbcTemplate(Mybatis 也可使用 JdbcTemplate 处理), 以商品查询为例 (仅简单使用示例， 具体业务实现可自行封装集成)

```java
import club.kingon.sql.builder.FromSQLBuilder;
import club.kingon.sql.builder.SQLBuilder;
import club.kingon.sql.builder.SqlBuilder;
import club.kingon.sql.builder.enums.Operator;
import club.kingon.sql.builder.spring.util.MapperUtils;
import club.kingon.sql.builder.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 商品信息
 */
class Goods {
    /**
     * 设置mysql字段映射
     */
    @Column("id")
    private Integer goodsId;

    private String goodsName;

    private BigDecimal price;

    /** 0:无效, 1:有效 **/
    private Integer status;
    // 省略getter setter
}

/**
 * 商品服务demo
 */
@Service
public class GoodsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static FromSQLBuilder PREFIX_BUILDER = SQLBuilder.selectAll().from("goods_tb");

    /**
     * 分页查询
     */
    public List<Goods> select(int pageNo, int pageSize) {
        return jdbcTemplate.query(
            PREFIX_BUILDER
                .limit((pageNo - 1) * pageSize, pageSize)
                .build()
            , MapperUtils.getMapper(Goods.class));
    }

    /**
     * 分页查询有效商品
     */
    public List<Goods> selectEffect(int pageNo, int pageSize) {
        return jdbcTemplate.query(
            PREFIX_BUILDER
                .where("status", Operator.EQ, 1)
                .limit((pageNo - 1) * pageSize, pageSize)
                .build()
            , MapperUtils.getMapper(Goods.class));
    }

    /**
     * 分页商品名搜索有效商品
     */
    public List<Goods> selectEffectByName(String likeGoodsName, int pageNo, int pageSize) {
        return jdbcTemplate.query(
            PREFIX_BUILDER
                .where("status", Operator.EQ, 1)
                .and("goods_name", Operator.LRLIKE, likeGoodsName)
                .limit((pageNo - 1) * pageSize, pageSize)
                .build()
            , MapperUtils.getMapper(Goods.class));
    }
}
```

### 使用内部提供的查询工具(语法类似Mybatis-Plus, 测试中, 生产环境暂不建议使用)
### PS: 查询的SqlBuilder参数必须通过QuerySqlBuilder.INSTANCE来构建, SelectSqlBuilder 必须通过SqlBuilder.select(...)构建

1. 配置文件 application.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://xxxx
    username: xxx
    password: xxx
    driver-class-name: xxxx
```
2. Application类 添加 Enable 注解
```java
import club.kingon.sql.builder.spring.annotation.EnableMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMapper
public class XXXApplication {
    public static void main(String[] args) {
        SpringApplication.run(XXXApplication.class, args);
    }
} 
```
3. 编写实体类
```java
import lombok.Data;
import club.kingon.sql.builder.annotation.Table;

@Data
@Table("xxx")
public class Item {

    private String category;

    private String goodsId;

    private String goodsName;
}
```
4. 编写dao层接口
```java
import club.kingon.sql.builder.spring.BaseMapper;
import club.kingon.sql.builder.spring.annotation.Mapper;
import com.example.entity.Item;

// 若接口实现了BaseMapper接口可不加@Mapper注解, 否则必须加上@Mapper注解
@Mapper
public interface ItemMapper extends BaseMapper<Item> {
}
```
5. 编写控制器测试

```java

import club.kingon.sql.builder.enums.Operator;
import club.kingon.sql.builder.spring.QuerySQLBuilder;
import com.example.dao.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    private ItemMapper mapper;

    @GetMapping("/xxx")
    public Object items() {
        return mapper.selectList(
            QuerySQLBuilder.INSTANCE
                .where("goods_id", Operator.IN, (1111, 2222, 3333, 4444))
        );
    }
}
```
