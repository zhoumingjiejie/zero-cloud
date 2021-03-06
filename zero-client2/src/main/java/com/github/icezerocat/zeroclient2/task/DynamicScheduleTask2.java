package com.github.icezerocat.zeroclient2.task;

import com.github.icezerocat.task.schedule.annotation.DynamicScheduled;
import com.github.icezerocat.task.schedule.thread.ScheduleTask;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@DynamicScheduled(cron = "*/4 * * * * ?")
@Component("dynamicScheduleTask2")
public class DynamicScheduleTask2 implements ScheduleTask {

    @SneakyThrows
    @Override
    public void run() {
        log.error("我是task2222，我需要执行 2s 钟的时间，我的线程的 id == > {}，时间 == >{}",
                Thread.currentThread().getId(), new Date());
        Thread.sleep(2000);
        log.error("task2222 ending ,我的线程的 id == > {} , 时间 == > {}",
                Thread.currentThread().getId(), new Date());
    }
}
