package com.github.icezerocat.icezerocat.jpa.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.icezerocat.icezerocat.jpa.entity.BaseEquipment;
import com.github.icezerocat.icezerocat.jpa.repository.BaseIpRep;
import com.github.icezerocat.icezerocat.jpa.repository.BaseRequirementRep;
import github.com.icezerocat.component.db.service.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Description: jpa
 * CreateDate:  2021/12/20 18:44
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class JpaController {

    final private BaseRequirementRep baseRequirementRep;
    final private DbService dbService;
    final private BaseIpRep baseIpRep;

    //@RequestMapping("/")
    public void index() {
        log.debug("{}", this.dbService.getTableInfo());
    }

    @GetMapping("say")
    public void say() {
        this.baseIpRep.findAll();
        Optional<BaseEquipment> byId = this.baseRequirementRep.findById("1");
        log.debug("{}", byId);
    }

    @RequestMapping("test")
    public Object test(String a, @RequestParam(required = false) Object b) {
        return a + "  =   " + b;
    }

    @RequestMapping("test2")
    public JSONObject test2(@RequestBody JSONObject o) {
        return o;
    }

    @RequestMapping("/")
    public Object jdbc() throws SQLException {
        Connection conn;
        Statement stmt;
        ResultSet rs;
        conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@192.168.0.11:1521/ORA11G",
                "QAS_R_GD",
                "raymon");
        stmt = conn.createStatement();
        log.debug("创建链接完成");
        rs = stmt.executeQuery("SELECT * FROM \"hot_words\"");
        log.debug("执行sql完成");

        List<String> wList = new ArrayList<>();
        while (rs.next()) {
            String theWord = rs.getString("word");
            log.debug(theWord);
            wList.add(theWord);
        }
        conn.close();
        stmt.close();
        rs.close();
        return wList;
    }

}
