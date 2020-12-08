package com.github.icezerocat.task.schedule.utils;

import com.github.icezerocat.zerocore.config.ApplicationContextZeroHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

/**
 * Description: 定时任务工具类
 * CreateDate:  2020/11/22 15:10
 *
 * @author zero
 * @version 1.0
 */
public class ScheduleUtil {
    /**
     * 缓存定时任务
     */
    private static Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    /**
     * 启动
     *
     * @param runnable 定时任务
     * @param corn     执行时间表达式
     */
    public static boolean start(Runnable runnable, String corn) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = ApplicationContextZeroHelper.getBean(ThreadPoolTaskScheduler.class);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler
                .schedule(runnable, new CronTrigger(corn));
        scheduledFutureMap.put(runnable.getClass().getName(), scheduledFuture);
        return true;
    }

    /**
     * 启动
     *
     * @param runnable 定时任务
     * @param corn     执行时间表达式
     * @param zone     地区
     */
    public static boolean start(Runnable runnable, String corn, String zone) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = ApplicationContextZeroHelper.getBean(ThreadPoolTaskScheduler.class);
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler
                .schedule(runnable, new CronTrigger(corn, TimeZone.getTimeZone(zone)));
        scheduledFutureMap.put(runnable.getClass().getName(), scheduledFuture);
        return true;
    }

    /**
     * 取消
     *
     * @param runnable 定时任务
     */
    public static boolean cancel(Runnable runnable) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(runnable.getClass().getName());
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
        scheduledFutureMap.remove(runnable.getClass().getName());
        return true;
    }

    /**
     * 修改
     *
     * @param runnable 定时任务
     * @param corn     执行时间表达式
     */
    public static boolean reset(Runnable runnable, String corn) {
        //先取消定时任务
        cancel(runnable);
        //然后启动新的定时任务
        start(runnable, corn);
        return true;
    }

    /**
     * 修改
     *
     * @param runnable 定时任务
     * @param corn     执行时间表达式
     * @param zone     时区
     */
    public static boolean reset(Runnable runnable, String corn, String zone) {
        //先取消定时任务
        cancel(runnable);
        //然后启动新的定时任务
        start(runnable, corn, zone);
        return true;
    }

}
