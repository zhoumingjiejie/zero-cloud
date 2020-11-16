package com.github.icezerocat.zerocore.config;

import com.github.icezerocat.zerocore.constants.AsyncPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

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
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}
