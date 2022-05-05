package com.github.icezerocat.zero.es.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.github.icezerocat.zero.es.entity.ZhfwQaQuestion;
import com.github.icezerocat.zero.es.mapper.ZhfwQaQuestionMapper;
import com.github.icezerocat.zero.es.repository.QaQuestionRep;
import com.github.icezerocat.zero.es.wrapper.ZhfwQaQuestionQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Description: 主题控制器
 * CreateDate:  2022/4/20 16:39
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class GuideThemeController {

    final private QaQuestionRep qaQuestionRep;
    final private ZhfwQaQuestionMapper zhfwQaQuestionMapper;

    /**
     * 初始化数据
     *
     * @return 初始化时间
     * @throws ExecutionException   执行异常
     * @throws InterruptedException 中断异常
     */
    @GetMapping("init")
    public String init() throws ExecutionException, InterruptedException {
        List<ZhfwQaQuestion> zhfwQaQuestions = this.zhfwQaQuestionMapper.listEntity(ZhfwQaQuestionQuery.query().selectAll());
        ExecutorService service = Executors.newCachedThreadPool();
        Future future = service.submit(
                () -> {
                    // ... do something inside runnable task
                    Iterable<ZhfwQaQuestion> questions = this.qaQuestionRep.saveAll(zhfwQaQuestions);
                    log.debug("{}", questions);
                    return 1;
                });
        service.shutdown();
        log.debug("future.get():{}", future.get());
        return new SimpleDateFormat().format(new Date());
    }

    public Object getEsData(){
        this.qaQuestionRep.findAll()
        return
    }
}
