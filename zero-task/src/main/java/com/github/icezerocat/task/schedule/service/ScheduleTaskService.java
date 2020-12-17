package com.github.icezerocat.task.schedule.service;

/**
 * Description: 定时任务服务
 * CreateDate:  2020/12/8 14:17
 *
 * @author zero
 * @version 1.0
 */
public interface ScheduleTaskService {

    /**
     * 启动定时任务-默认：配置的包名
     */
    void startTask();

    /**
     * 启动定时任务
     *
     * @param scanBasePackages 需要扫描的包路径
     */
    void startTask(Object... scanBasePackages);
}
