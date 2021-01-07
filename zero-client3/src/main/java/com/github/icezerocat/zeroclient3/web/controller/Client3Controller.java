package com.github.icezerocat.zeroclient3.web.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.icezerocat.zeroclient3.entity.DynamicVersion;
import com.github.icezerocat.zeroclient3.service.DynamicVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;

/**
 * Description: 客户端3控制器
 * CreateDate:  2020/12/21 15:16
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("client3")
public class Client3Controller {

    @Resource
    private Cache<String, Object> caffeineCache;
    @Resource
    private DynamicVersionService dynamicVersionService;

    @GetMapping("select")
    public void select() {
        DynamicVersion dynamicVersion = new DynamicVersion();
        dynamicVersion.setId(0L);
        dynamicVersion.setCreateTime(new Date());
        this.dynamicVersionService.saveOrUpdateBatch(Collections.singletonList(dynamicVersion));
        /*for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                List<DynamicVersion> list = this.dynamicVersionService.list();
                if (finalI - 3 == 0) {
                    this.dynamicVersionService.getOne(null);
                }
            });
            thread.start();
        }*/
        //log.debug("{}", list);
    }

    @GetMapping("setCaffeine")
    public void setCaffeine() {
        this.dynamicVersionService.getOne(null);
        log.debug("[{}]-{}", Thread.currentThread().getName(), Thread.currentThread().getId());
        this.caffeineCache.put(Thread.currentThread().getName(), new Date());
        log.debug("value:{}", this.caffeineCache.getIfPresent(Thread.currentThread().getName()));
    }
}
