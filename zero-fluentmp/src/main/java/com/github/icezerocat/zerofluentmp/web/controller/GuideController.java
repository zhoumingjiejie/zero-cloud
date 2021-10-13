package com.github.icezerocat.zerofluentmp.web.controller;

import com.github.icezerocat.zerofluentmp.entity.TGuideCatalog;
import com.github.icezerocat.zerofluentmp.mapper.TGuideCatalogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description: 主题控制器
 * CreateDate:  2021/10/13 9:36
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
public class GuideController {

    @Resource
    private TGuideCatalogMapper tGuideCatalogMapper;

    @GetMapping("findOne")
    public String findOne() {
        TGuideCatalog one = this.tGuideCatalogMapper.findOne(
                this.tGuideCatalogMapper.query().where.catalogId().eq("1690926c73295d590173295de924017e")
                        .end());
        return String.valueOf(one);
    }
}
