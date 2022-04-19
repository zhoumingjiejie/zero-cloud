package com.github.icezerocat.zero.id.web.controller;

import com.github.icezerocat.zero.id.model.IdBlock;
import com.github.icezerocat.zero.id.service.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    final private IdGeneratorService idGeneratorService;

    @GetMapping("say")
    public String say() {
        ExecutorService executorService = Executors.newFixedThreadPool(500);
        log.debug("线程池创建：{}", executorService);
        IdBlock idBlocka = this.idGeneratorService.doGetNextIdBlock();
        log.debug("{}——{}", Thread.currentThread().getName(), idBlocka);
        for (int i = 0; i < 800; i++) {
            executorService.submit(() -> {
                IdBlock idBlock = this.idGeneratorService.doGetNextIdBlock();
                log.warn("{}——{}", Thread.currentThread().getName(), idBlock);
            });
        }
        for (int i = 0; i < 800; i++) {
            executorService.submit(() -> {
                IdBlock idBlock = this.idGeneratorService.doGetNextIdBlock("zero", "zero");
                log.warn("{}——{}", Thread.currentThread().getName(), idBlock);
            });
        }
        return "say";
    }
}
