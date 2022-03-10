package com.github.icezerocat.zero.jdbc.web.controller;

import com.github.icezerocat.zero.jdbc.builder.FromSQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;
import com.github.icezerocat.zero.jdbc.builder.spring.util.MapperUtils;
import com.github.icezerocat.zero.jdbc.entity.TGuideTheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: jdbcTemplate
 * CreateDate:  2022/1/25 17:09
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("jt")
public class JdbcTemplateController {

    final private JdbcTemplate jdbcTemplate;

    private final static FromSQLBuilder PREFIX_BUILDER = SQLBuilder.selectAll().from("t_guide_theme");

    /**
     * 分页查询
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @return 分页数据
     */
    @RequestMapping("select")
    public List select(int pageNo, int pageSize) {
        SQLBuilder sqlBuilder =
                SQLBuilder.model(TGuideTheme.class);
        log.debug("{}", sqlBuilder.precompileSQL());
        log.debug("{}", sqlBuilder.precompileArgs());
        return this.jdbcTemplate.query(
                PREFIX_BUILDER
                        .where(TGuideTheme.IS_OPEN, Operator.EQ, 1)
                        .limit((pageNo - 1) * pageSize, pageSize)
                        .build()
                , MapperUtils.getMapper(TGuideTheme.class));
    }
}
