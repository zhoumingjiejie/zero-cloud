package com.github.icezerocat.zero.thrad.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.math.BigDecimal;
import java.util.concurrent.*;

/**
 * Description: 线程池工具类
 * CreateDate:  2022/4/9 14:54
 *
 * @author zero
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ThreadPoolUtils {

    /**
     * 线程池执行者，使用volatile关键字保其可见性(不推荐)
     */
    volatile private static ThreadPoolTaskExecutor defaultThreadPool = null;

    /**
     * cpu密集型线程池
     */
    volatile private static ThreadPoolTaskExecutor cpuThreadPool = null;

    /**
     * io密集型线程池
     */
    volatile private static ThreadPoolTaskExecutor ioThreadPool = null;

    /**
     * 获取处理器数量
     */
    private static int CPU_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 每秒任务数300个
     */
    private static int tasks = 300;

    /**
     * 每个任务花费的时间0.1s
     */
    private static double taskCost = 0.1d;

    /**
     * 系统允许容忍的最大响应时间1s
     */
    private static double responseTime = 1d;

    /**
     * 设置每秒任务数
     *
     * @param tasks 任务数
     */
    public static void setTasks(int tasks) {
        ThreadPoolUtils.tasks = tasks;
    }

    /**
     * 设置每个任务花费的时间，单位秒
     *
     * @param taskCost 每个任务花费的时间，单位秒
     */
    public static void setTaskCost(float taskCost) {
        ThreadPoolUtils.taskCost = taskCost;
    }

    /**
     * 系统允许容忍的最大响应时间，单位秒
     *
     * @param responseTime 系统允许容忍的最大响应时间，单位秒
     */
    public static void setResponseTime(int responseTime) {
        ThreadPoolUtils.responseTime = responseTime;
    }

    /**
     * 构建线程池执行器单例
     *
     * @return 线程池执行器
     */
    public static ThreadPoolTaskExecutor getDefaultThreadPool() {
        if (defaultThreadPool == null) {
            synchronized (ThreadUtils.class) {
                if (defaultThreadPool == null) {
                    // 默认活跃时间60s
                    defaultThreadPool = new ThreadPoolTaskExecutor();
                    // defaultThreadPool
                    defaultThreadPool.setCorePoolSize(getCorePoolSize());
                    // 最大线程数
                    defaultThreadPool.setMaxPoolSize(getCorePoolSize());
                    // 队列容量
                    defaultThreadPool.setQueueCapacity(getQueueCapacity());
                    // 线程名称前缀
                    defaultThreadPool.setThreadNamePrefix("Default-Thread_");
                    // 拒绝策略,不在新线程中执行任务，而是调用者所在的线程来执行
                    defaultThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                    // 加载初始化
                    defaultThreadPool.initialize();
                }
            }
        }
        return defaultThreadPool;
    }

    /**
     * 单例模式获取cpu密集型线程池
     *
     * @return cpu密集型线程池
     */
    public static ThreadPoolTaskExecutor getCpuThreadPool() {
        if (cpuThreadPool == null) {
            synchronized (ThreadUtils.class) {
                if (cpuThreadPool == null) {
                    // 默认活跃时间60s
                    cpuThreadPool = new ThreadPoolTaskExecutor();
                    // 核心线程数
                    cpuThreadPool.setCorePoolSize(CPU_NUM);
                    // 最大线程数
                    cpuThreadPool.setMaxPoolSize(CPU_NUM + 1);
                    // 队列容量
                    cpuThreadPool.setQueueCapacity(getQueueCapacity());
                    // 线程名称前缀
                    cpuThreadPool.setThreadNamePrefix("Default-Cpu-Thread_");
                    // 拒绝策略,不在新线程中执行任务，而是调用者所在的线程来执行
                    cpuThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                    // 加载初始化
                    cpuThreadPool.initialize();
                }
            }
        }
        return cpuThreadPool;
    }

    /**
     * 单例模式获取io密集型线程池
     *
     * @return io密集型线程池
     */
    public static ThreadPoolTaskExecutor getIoThreadPool() {
        if (ioThreadPool == null) {
            synchronized (ThreadUtils.class) {
                if (ioThreadPool == null) {
                    // 默认活跃时间60s
                    ioThreadPool = new ThreadPoolTaskExecutor();
                    // 核心线程数
                    ioThreadPool.setCorePoolSize(CPU_NUM);
                    // 最大线程数
                    ioThreadPool.setMaxPoolSize(CPU_NUM * 2);
                    // 队列容量
                    ioThreadPool.setQueueCapacity(getQueueCapacity());
                    // 线程名称前缀
                    ioThreadPool.setThreadNamePrefix("Default-Io-Thread_");
                    // 拒绝策略,不在新线程中执行任务，而是调用者所在的线程来执行
                    ioThreadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
                    // 加载初始化
                    ioThreadPool.initialize();
                }
            }
        }
        return ioThreadPool;
    }

    /**
     * 获取核心线程数 = tasks / (1/taskCost)
     *
     * @return 核心线程数
     */
    private static int getCorePoolSize() {
        BigDecimal bd1 = new BigDecimal(1);
        BigDecimal taskCostBd = new BigDecimal(taskCost);
        BigDecimal tasksBd = new BigDecimal(tasks);
        int threadCount = tasksBd.divide(bd1.divide(taskCostBd, 2, BigDecimal.ROUND_HALF_UP), 0, BigDecimal.ROUND_HALF_UP).intValue();
        System.out.println("线程池线程总数：" + threadCount);
        return threadCount;
    }

    /**
     * 获取队列容量 = (coreSizePool/taskCost) * responseTime
     *
     * @return 队列容量大小
     */
    private static int getQueueCapacity() {
        BigDecimal coreSizePoolBd = new BigDecimal(getCorePoolSize());
        BigDecimal taskCostBd = new BigDecimal(taskCost);
        BigDecimal responseTimeBd = new BigDecimal(responseTime);
        int queueCapacity = coreSizePoolBd.divide(taskCostBd, 2, BigDecimal.ROUND_HALF_UP).multiply(responseTimeBd).intValue();
        System.out.println("线程池队列容量：" + queueCapacity);
        return queueCapacity;
    }
}

