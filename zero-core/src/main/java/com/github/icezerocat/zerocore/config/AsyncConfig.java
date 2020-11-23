package com.github.icezerocat.zerocore.config;

import com.github.icezerocat.zerocore.constants.AsyncPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ProjectName: [factory-boot-master]
 * Package:     [cn.oz.factory.factoryalibaba.config.AsyncConfig]
 * Description: 线程池
 * CreateDate:  2020/4/15 10:32
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements SchedulingConfigurer, AsyncConfigurer {

    @Value("${async.corePoolSize:5}")
    private int corePoolSize;
    @Value("${async.maxPoolSize:10}")
    private int maxPoolSize;
    @Value("${async.queueCapacity:20}")
    private int queueCapacity;
    @Value("${async.keepAliveSeconds:60}")
    private int keepAliveSeconds;

    @Bean(name = AsyncPool.POOL_SCHEDULE)
    public Executor schedulePool() {
        return initExecutor("scheduleThread-");
    }

    /**
     * 异步任务中异常处理
     *
     * @return 异常处理（输出异常信息）
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("==========================" + ex.getMessage() + "=======================", ex);
            log.error("exception method:" + method.getName());
        };
    }


    /**
     * 初始化pool参数
     *
     * @param threadNamePrefix 线程名字前缀
     * @return executor
     */
    private Executor initExecutor(String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(queueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略(rejection-policy：当pool已经达到max size的时候，如何处理新任务;CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行)
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

    /**
     * 配置Scheduled线程池任务
     *
     * @param scheduledTaskRegistrar scheduled任务注册器
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        TaskScheduler taskScheduler = threadPoolTaskScheduler();
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler);
    }

    /**
     * 并行任务使用策略：多线程处理
     *
     * @return ThreadPoolTaskScheduler 线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(corePoolSize);
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

}
