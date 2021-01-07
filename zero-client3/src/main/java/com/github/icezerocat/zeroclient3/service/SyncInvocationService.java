package com.github.icezerocat.zeroclient3.service;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.InvocationTargetException;

/**
 * Description: 同步调用service
 * CreateDate:  2020/12/22 11:36
 *
 * @author zero
 * @version 1.0
 */
public interface SyncInvocationService {

    /**
     * 调用同步处理方法进行处理，调用失败时直接入db库
     *
     * @param joinPoint 切面连接点
     */
    void syncInvocation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    /**
     * 方法异常回调处理函数
     *
     * @param ex 异常
     */
    void doAfterThrowing(Throwable ex);
}
