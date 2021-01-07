package com.github.icezerocat.zeroclient3.intercept;

import com.github.icezerocat.zeroclient3.service.SyncInvocationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Description: 数据同步提供者切面
 * CreateDate:  2020/12/21 12:01
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
public class SyncProviderAspect {

    private final SyncInvocationService syncInvocationService;

    public SyncProviderAspect(SyncInvocationService syncInvocationService) {
        this.syncInvocationService = syncInvocationService;
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service.IService.*(..))")
    private void iService() {
    }

    @Around("iService()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        this.syncInvocationService.syncInvocation(joinPoint);
        return joinPoint.proceed(joinPoint.getArgs());
    }

    @AfterThrowing(pointcut = "iService()", throwing = "ex")
    public void doAfterThrowing(Throwable ex) {
        this.syncInvocationService.doAfterThrowing(ex);
        log.error("doAfterThrowing:", ex);
        ex.printStackTrace();
    }

}
