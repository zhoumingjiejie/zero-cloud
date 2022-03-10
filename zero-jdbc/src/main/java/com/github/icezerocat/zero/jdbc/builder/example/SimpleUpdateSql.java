package com.github.icezerocat.zero.jdbc.builder.example;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;

import java.util.Arrays;

/**
 * @author dragons
 * @date 2021/11/11 20:24
 */
public class SimpleUpdateSql {
    public static void main(String[] args) {
        SQLBuilder builder = SQLBuilder
            .update("table_a")
            .set("column_1", 18)
            .where("column_2", Operator.EQ, "test");
        System.out.println(builder.build());
        System.out.println(builder.precompileSQL());
        System.out.println(Arrays.toString(builder.precompileArgs()));
    }
}
