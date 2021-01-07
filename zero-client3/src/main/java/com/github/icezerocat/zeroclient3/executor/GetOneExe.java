package com.github.icezerocat.zeroclient3.executor;

import com.github.icezerocat.zeroclient3.intercept.AbstractSyncProviderExe;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Description: GetOne方法处理
 * CreateDate:  2020/12/22 9:20
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
public class GetOneExe extends AbstractSyncProviderExe {

    @Override
    public void doAround(ProceedingJoinPoint joinPoint) {

    }


    @Override
    public void doAfterThrowing(Throwable ex) {
        log.debug("getOne:", ex);
    }
}
