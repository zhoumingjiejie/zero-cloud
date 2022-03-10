package com.github.icezerocat.zero.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class ZeroJdbcApplication {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(ZeroJdbcApplication.class, args);
        jdbc();
    }

    private static void jdbc() throws SQLException {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.13:1521:ORA11G",
                "ARASK",
                "raymon");
        stmt = conn.createStatement();
//        rs = stmt.executeQuery("SELECT * FROM \"hot_words\"");
        rs = stmt.executeQuery("SELECT * FROM \"QA_DYHD\" WHERE HDID = '97ECD89367296EABE053068A0C649BDE'");

        while (rs.next()) {
//            String theWord = rs.getString("word");
//            System.out.println(theWord);

            Arrays.asList("HDWZ", "HDWX", "HDZX").forEach(o->{
                Clob clob = null;//Java.sql.Clob
                try {
                    clob = rs.getClob(o);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String detailinfo = "";
                if (clob != null) {
                    try {
                        detailinfo = clob.getSubString(1, (int) clob.length());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(detailinfo);
            });

        }
        conn.close();
        stmt.close();
        rs.close();
    }

}
