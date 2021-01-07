package com.github.icezerocat.zeroclient3.intercept;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Description: 同步数据提供者，执行处理逻辑，抽象类
 * CreateDate:  2020/12/21 18:26
 *
 * @author zero
 * @version 1.0
 */
public abstract class AbstractSyncProviderExe {

    /**
     * 环绕方法：监听被调用的方法
     *
     * @param joinPoint 方法参数等数据
     */
    public abstract void doAround(ProceedingJoinPoint joinPoint);

    /**
     * 方法执行异常处理
     *
     * @param ex 异常
     */
    public abstract void doAfterThrowing(Throwable ex);
}
