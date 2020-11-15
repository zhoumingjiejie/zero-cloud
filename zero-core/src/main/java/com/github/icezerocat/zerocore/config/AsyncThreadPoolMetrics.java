package com.github.icezerocat.zerocore.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ProjectName: [factory-boot-master]
 * Package:     [cn.oz.factory.factoryalibaba.config.AsyncThreadPoolMetrics]
 * Description: 异步线程池监控
 * CreateDate:  2020/4/16 9:27
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class AsyncThreadPoolMetrics {

    private final ApplicationContext applicationContext;

    public AsyncThreadPoolMetrics(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 所有异步线程池
     *
     * @return 所有异步线程池信息
     */
    public List<ThreadPoolInfo> asyncThreadPool() {
        List<ThreadPoolInfo> threadPoolInfoList = new ArrayList<>();
        Map<String, ThreadPoolTaskExecutor> taskExecutorMap = this.applicationContext.getBeansOfType(ThreadPoolTaskExecutor.class);
        for (Map.Entry<String, ThreadPoolTaskExecutor> taskExecutorEntry : taskExecutorMap.entrySet()) {
            threadPoolInfoList.add(this.getThreadPoolInfo(taskExecutorEntry.getKey(), taskExecutorEntry.getValue()));
        }
        return threadPoolInfoList;
    }

    /**
     * 异步线程池
     *
     * @param threadPoolName 线程池名称名字
     * @return 异步线程池信息
     */
    public ThreadPoolInfo asyncThreadPool(String threadPoolName) {
        Executor executor = this.applicationContext.getBean(threadPoolName, Executor.class);
        Object[] objects = {executor};
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
        for (Object o : objects) {
            ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor) o;
            threadPoolInfo = this.getThreadPoolInfo(threadPoolName, threadPoolTaskExecutor);
        }
        return threadPoolInfo;
    }

    /**
     * 判断当前线程池是否空闲
     *
     * @param threadPoolName 线程池名称
     * @return 是否空闲（true：空闲）
     */
    public boolean isNull(String threadPoolName) {
        ThreadPoolInfo threadPoolInfo = this.asyncThreadPool(threadPoolName);
        return threadPoolInfo.getActiveCount() == 0 && threadPoolInfo.getQueueSize() == 0;
    }

    /**
     * 获取线程池信息
     *
     * @param beanName               beanName
     * @param threadPoolTaskExecutor 线程池任务执行器
     * @return 线程池信息
     */
    private ThreadPoolInfo getThreadPoolInfo(String beanName, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
        ThreadPoolExecutor threadPoolExecutor = threadPoolTaskExecutor.getThreadPoolExecutor();
        //beanName
        threadPoolInfo.setBeanName(beanName);
        //提交任务数
        threadPoolInfo.setTaskCount(threadPoolExecutor.getTaskCount());
        //完成任务数
        threadPoolInfo.setCompletedTaskCount(threadPoolExecutor.getCompletedTaskCount());
        //当前有多少线程正在处理任务
        threadPoolInfo.setActiveCount(threadPoolExecutor.getActiveCount());
        //还剩多少个任务未执行
        threadPoolInfo.setQueueSize(threadPoolExecutor.getQueue().size());
        //当前可用队列长度
        threadPoolInfo.setQueueRemainingCapacity(threadPoolExecutor.getQueue().remainingCapacity());
        threadPoolInfo.setDate(new Date());
        return threadPoolInfo;
    }

    /**
     * 线程池信息
     */
    @Data
    public static class ThreadPoolInfo {
        private String beanName;
        private long taskCount;
        private long completedTaskCount;
        private int activeCount;
        private int queueSize;
        private int queueRemainingCapacity;
        private Date date;
    }
}
