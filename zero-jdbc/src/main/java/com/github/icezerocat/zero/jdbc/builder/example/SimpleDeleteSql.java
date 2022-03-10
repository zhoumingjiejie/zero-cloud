package com.github.icezerocat.zero.jdbc.builder.example;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;

import java.util.Arrays;

/**
 * @author dragons
 * @date 2021/11/11 20:23
 */
public class SimpleDeleteSql {
    public static void main(String[] args) {
        SQLBuilder builder = SQLBuilder
            .delete("table_a")
            .where("column_1", Operator.GE, 10);
        System.out.println(builder.build());
        System.out.println(builder.precompileSQL());
        System.out.println(Arrays.toString(builder.precompileArgs()));
    }
}
