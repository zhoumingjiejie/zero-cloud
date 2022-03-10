package com.github.icezerocat.zero.jdbc.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

/**
 * Description: jdbc
 * CreateDate:  2022/1/10 18:05
 *
 * @author zero
 * @version 1.0
 */
@RestController
public class JdbcController {

    @RequestMapping("/")
    public void index() throws SQLException {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.11:1521:ORA11G",
                "QAS_R_GD",
                "raymon");
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM \"hot_words\"");

        while (rs.next()) {
            String theWord = rs.getString("word");
            System.out.println(theWord);
        }
        conn.close();
        stmt.close();
        rs.close();
    }
}
