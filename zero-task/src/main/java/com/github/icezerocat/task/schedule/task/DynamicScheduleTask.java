package com.github.icezerocat.task.schedule.task;

import com.github.icezerocat.task.schedule.annotation.DynamicScheduled;
import com.github.icezerocat.task.schedule.thread.ScheduleTask;
import com.github.icezerocat.zerocore.constants.AsyncPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description: 动态schedule任务
 * CreateDate:  2020/11/22 22:47
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Async(AsyncPool.POOL_SCHEDULE)
@DynamicScheduled(cron = "*/2 * * * * ?")
@Component("dynamicScheduleTask")
public class DynamicScheduleTask implements ScheduleTask {

    @SneakyThrows
    @Override
    public void run() {
        log.error("我是task1111，我需要执行 10s 钟的时间，我的线程的 id == > {}，时间 == >{}", Thread.currentThread().getId(), new Date());
        Thread.sleep(10000);
        log.error("task1111 ending ,我的线程的 id == > {} , 时间 == > {}", Thread.currentThread().getId(), new Date());
    }
}
