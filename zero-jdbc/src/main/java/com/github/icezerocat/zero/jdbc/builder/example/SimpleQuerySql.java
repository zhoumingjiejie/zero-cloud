package com.github.icezerocat.zero.jdbc.builder.example;


import com.github.icezerocat.zero.jdbc.builder.Conditions;
import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.enums.Order;

import java.util.Arrays;

/**
 * @author dragons
 * @date 2021/11/11 10:31
 */
public class SimpleQuerySql {
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
        System.out.println(builder.build());
        System.out.println(builder.precompileSQL());
        System.out.println(Arrays.toString(builder.precompileArgs()));
    }
}
