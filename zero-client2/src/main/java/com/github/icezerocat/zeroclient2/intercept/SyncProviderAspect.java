package com.github.icezerocat.zeroclient2.intercept;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
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

    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.saveOrUpdateBatch(*))")
    private void iServiceToSaveOrUpdateBatch() {
    }

    @Pointcut("execution(* github.com.icezerocat.mybatismp.common.mybatisplus.NoahServiceImpl.saveOrUpdateBatch(*))")
    private void noahServiceImplToSaveOrUpdateBatch() {
    }

    @Pointcut("execution(* com.baomidou.mybatisplus.extension.service.IService.list(*))")
    private void list() {
    }


    @Around("iServiceToSaveOrUpdateBatch() || noahServiceImplToSaveOrUpdateBatch() || list()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        log.debug("参数：{}", args);
        return joinPoint.proceed(args);
    }
}
