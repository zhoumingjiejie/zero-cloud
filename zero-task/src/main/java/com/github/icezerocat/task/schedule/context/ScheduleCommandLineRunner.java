package com.github.icezerocat.task.schedule.context;

import com.github.icezerocat.task.schedule.service.ScheduleTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Description: 定时任务初始化启动器
 * CreateDate:  2020/11/22 17:06
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class ScheduleCommandLineRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final ScheduleTaskService scheduleTaskService;

    public ScheduleCommandLineRunner(ScheduleTaskService scheduleTaskService) {
        this.scheduleTaskService = scheduleTaskService;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
        this.scheduleTaskService.startTask();
    }
}
