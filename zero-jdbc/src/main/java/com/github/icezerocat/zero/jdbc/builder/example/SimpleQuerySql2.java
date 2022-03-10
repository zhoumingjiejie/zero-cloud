package com.github.icezerocat.zero.jdbc.builder.example;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.annotation.Query;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;

import java.util.Arrays;
import java.util.List;

class Item {
    Integer id;
    String name;

    public Item(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}

class QueryCriteria {
    @Query
    private Integer id;
    @Query(type = Operator.LRLIKE)
    private String name;
    @Query(value = "item_id", type = Operator.IN, attr = "id")
    private List<Item> items;

    public QueryCriteria(Integer id, String name, List<Item> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }
}

/**
 * @author dragons
 * @date 2022/1/18 11:24
 */
public class SimpleQuerySql2 {
    public static void main(String[] args) {
        SQLBuilder builder = SQLBuilder.selectAll()
            .from("test_tb")
            .where(new QueryCriteria(1, "dragons", Arrays.asList(new Item(1, "xxx"), new Item(2, "xx"))))
            .orLRlike("keyword", "shouji")
            .andBetween("xxx", 1, 5);
        System.out.println(builder.build());
        System.out.println(builder.precompileSQL());
        System.out.println(Arrays.toString(builder.precompileArgs()));
    }
}
