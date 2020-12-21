package com.github.icezerocat.component.redisson.controller;

import com.github.icezerocat.component.redisson.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Description: 锁控制器
 * CreateDate:  2020/12/17 17:38
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("lock")
public class LockController {

    private final DistributedLocker distributedLocker;

    public LockController(DistributedLocker distributedLocker) {
        this.distributedLocker = distributedLocker;
    }

    /**
     * 一直锁
     */
    @RequestMapping("/lock")
    public void lock() {
        String key = "redisson_key";
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                try {
                    log.debug("=============线程开启============" + Thread.currentThread().getName());
                    distributedLocker.lock(key, 10L); //直接加锁，获取不到锁则一直等待获取锁
                    Thread.sleep(100); //获得锁之后可以进行相应的处理
                    log.debug("======获得锁后进行相应的操作======" + Thread.
                            currentThread().getName());
                    distributedLocker.unlock(key); //解锁
                    log.debug("=============================" +
                            Thread.currentThread().getName());
                    boolean isGetLock = distributedLocker.tryLock(key, TimeUnit.SECONDS, 5L, 10L); // 尝试获取锁，等待5秒，自己获得锁后一直不解锁则10秒后自动解锁
                    if (isGetLock) {
                        log.debug("线程:" + Thread.currentThread().getName() + ",获取到了锁");
                        Thread.sleep(100); // 获得锁之后可以进行相应的处理
                        log.debug("======获得租期锁后进行相应的操作======" + Thread.currentThread().getName());
                        //distributedLocker.unlock(key);
                        log.debug("=============================" + Thread.currentThread().getName());
                    } else {
                        log.debug("{}:尝试获取锁失败！", Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }

    /**
     * 租期锁
     */
    @RequestMapping("leaseLock")
    public void leaseLock() {
        String key = "redisson_key";
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(() -> {
                try {
                    log.debug("=============leaseLock线程开启============" + Thread.currentThread().getName());
                    boolean isGetLock = distributedLocker.tryLock(key, TimeUnit.SECONDS, 5L, 10L); // 尝试获取锁，等待5秒，自己获得锁后一直不解锁则10秒后自动解锁
                    if (isGetLock) {
                        log.debug("leaseLock线程:" + Thread.currentThread().getName() + ",获取到了锁");
                        Thread.sleep(100); // 获得锁之后可以进行相应的处理
                        log.debug("======leaseLock获得锁后进行相应的操作======" + Thread.currentThread().getName());
                        //distributedLocker.unlock(key);
                        log.debug("=============================" + Thread.currentThread().getName());
                    } else {
                        log.debug("{}:leaseLock尝试获取锁失败！", Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }

}
