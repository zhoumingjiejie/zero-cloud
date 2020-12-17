package com.github.icezerocat.task.schedule.service.impl;

import com.github.icezerocat.task.schedule.annotation.DynamicScheduled;
import com.github.icezerocat.task.schedule.config.ScheduleProperty;
import com.github.icezerocat.task.schedule.service.ScheduleTaskService;
import com.github.icezerocat.task.schedule.thread.ScheduleTask;
import com.github.icezerocat.task.schedule.utils.ScheduleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * Description: 定时任务服务Impl
 * CreateDate:  2020/12/8 14:17
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {

    @Resource
    private ScheduleProperty scheduleProperty;

    @Override
    public void startTask() {
        if (this.scheduleProperty != null && !CollectionUtils.isEmpty(this.scheduleProperty.getScanBasePackages())) {
            this.startTask(this.scheduleProperty.getScanBasePackages());
        }
    }

    @Override
    public void startTask(Object... scanBasePackages) {
        Reflections reflections = new Reflections(scanBasePackages);
        Set<Class<? extends ScheduleTask>> subTypesOf = reflections.getSubTypesOf(ScheduleTask.class);
        this.scheduleTask(subTypesOf);
    }

    /**
     * 执行schedule任务
     *
     * @param classSet 任务类
     */
    private void scheduleTask(Set<Class<? extends ScheduleTask>> classSet) {
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
