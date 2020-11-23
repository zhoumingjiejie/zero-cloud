package com.github.icezerocat.task.schedule.context;

import com.github.icezerocat.task.schedule.annotation.DynamicScheduled;
import com.github.icezerocat.task.schedule.thread.ScheduleTask;
import com.github.icezerocat.task.schedule.utils.ScheduleUtil;
import com.github.icezerocat.zerocommon.utils.PackageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

/**
 * Description: 定时任务初始化启动器
 * CreateDate:  2020/11/22 17:06
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component
public class ScheduleCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        //获取动态定时任务类
        Set<Class<? extends ScheduleTask>> classSet = PackageUtil.getClassByName(ScheduleTask.class);
        classSet.forEach(c -> {
            ScheduleTask scheduleTask = null;
            try {
                //获取定时任务实体类（运行类）
                scheduleTask = c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("获取动态定时任务类[{}]出错：{}", c.getName(), e.getMessage());
                e.printStackTrace();
            }
            final ScheduleTask finalScheduleTask = scheduleTask;
            Optional.ofNullable(c.getAnnotation(DynamicScheduled.class)).ifPresent(dynamicScheduled -> {
                //启动定时任务
                if (StringUtils.isNotBlank(dynamicScheduled.cron()) && finalScheduleTask != null) {
                    log.info("ScheduleCommandLineRunner执行定时任务：{}", c.getName());
                    if (StringUtils.isNotBlank(dynamicScheduled.zone())) {
                        ScheduleUtil.start(finalScheduleTask, dynamicScheduled.cron(), dynamicScheduled.zone());
                    } else {
                        ScheduleUtil.start(finalScheduleTask, dynamicScheduled.cron());
                    }
                }
            });
        });

    }
}
