package com.github.icezerocat.zero.id.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.icezerocat.zero.id.model.IdBlock;
import github.com.icezerocat.jdbctemplate.service.BaseJdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description: 序列号控制器
 * CreateDate:  2021/12/31 11:55
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class SerialNumberController {

    final private BaseJdbcTemplate baseJdbcTemplate;

    @GetMapping("say")
    public String say() {
        List<Map<String, Object>> mapList = this.baseJdbcTemplate.queryForList("select * from t_guide_catalog");
        log.debug("{}", mapList);
        IdBlock idBlock = new IdBlock("zero");
        int[] insert = this.baseJdbcTemplate.insert(idBlock);
        log.debug("返回值：{}=====>{}", insert, idBlock);
        return JSONObject.toJSONString(mapList);
    }
}
