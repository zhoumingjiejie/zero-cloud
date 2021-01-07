package com.github.icezerocat.zeroclient3.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.icezerocat.component.redisson.service.DistributedLocker;
import com.github.icezerocat.component.redisson.utils.RedisUtil;
import com.github.icezerocat.zeroclient3.config.SyncApplicationContextHelper;
import com.github.icezerocat.zeroclient3.config.SyncProperty;
import com.github.icezerocat.zeroclient3.constant.CacheConstant;
import com.github.icezerocat.zeroclient3.service.SyncInvocationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Description: 同步调用impl
 * CreateDate:  2020/12/22 11:36
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("syncInvocationService")
public class SyncInvocationServiceImpl implements SyncInvocationService {

    private final SyncProperty syncProperty;
    private final RedissonClient redissonClient;
    private final DistributedLocker distributedLocker;
    private final Cache<String, Object> caffeineCache;

    public SyncInvocationServiceImpl(SyncProperty syncProperty, RedissonClient redissonClient, DistributedLocker distributedLocker, Cache<String, Object> caffeineCache) {
        this.syncProperty = syncProperty;
        this.redissonClient = redissonClient;
        this.distributedLocker = distributedLocker;
        this.caffeineCache = caffeineCache;
    }

    @Override
    public void syncInvocation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // 获取目标类名称
        String clazzName = joinPoint.getTarget().getClass().getName();
        // 获取目标类方法名称"
        String methodName = joinPoint.getSignature().getName();
        String key = clazzName + "." + methodName;
        log.debug("SyncProviderAspect监听：{}", key);
        if (this.syncProperty != null && this.syncProperty.getMethodMapBean().containsKey(key)) {
            try {
                Object o = SyncApplicationContextHelper.getBean(this.syncProperty.getMethodMapBean().get(key));
                assert o != null;
                this.caffeineCache.put(Thread.currentThread().getName(), o);
                Method doAround = o.getClass().getMethod("doAround", ProceedingJoinPoint.class);
                doAround.setAccessible(true);
                RLock fairLock = this.redissonClient.getFairLock(CacheConstant.WX_SYNC_LOCK.getValue());
                boolean tryLock = this.distributedLocker.tryLock(fairLock, TimeUnit.SECONDS, 5L, 15L);
                //判断同步数据是否加锁
                if (tryLock) {
                    RedisUtil.getInstance().set(CacheConstant.WX_SYNC_LOCK_UPDATE_DATE.getValue(), new Date(), 30000L);
                    doAround.invoke(o, joinPoint);
                    fairLock.unlock();
                } else {
                    //判断锁是否还有效，无效则强制解锁
                    if (RedisUtil.getInstance().get(CacheConstant.WX_SYNC_LOCK_UPDATE_DATE.getValue()) != null
                            && RedisUtil.getInstance().getExpire(CacheConstant.WX_SYNC_LOCK_UPDATE_DATE.getValue()) < System.currentTimeMillis()) {
                        RedisUtil.getInstance().set(CacheConstant.WX_SYNC_LOCK_UPDATE_DATE.getValue(), new Date(), 30000L);
                        doAround.invoke(o, joinPoint);
                        fairLock.forceUnlock();
                    } else {
                        //TODO 锁获取失败，直接插入数据库（table：id，JSON.toJSONString(joinPoint),class_full_name,method_name,create_date,status）
                        RedisUtil.getInstance().set(CacheConstant.WX_SYNC_DB_DATA.getValue(), true);
                    }
                }
            } catch (Exception e) {
                //TODO 局部处理逻辑失败，直接插入数据库（table：id，JSON.toJSONString(joinPoint),class_full_name,method_name,create_date,status）
                RedisUtil.getInstance().set(CacheConstant.WX_SYNC_DB_DATA.getValue(), true);
                log.error("(wx data sync execution failed)数据同步处理逻辑执行失败！", e);
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public void doAfterThrowing(Throwable ex) {
        Object o = this.caffeineCache.getIfPresent(Thread.currentThread().getName());
        if (o != null) {
            try {
                Method doAfterThrowing = o.getClass().getMethod("doAfterThrowing", Throwable.class);
                doAfterThrowing.setAccessible(true);
                doAfterThrowing.invoke(o, ex);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                //TODO 局部异常处理失败，redis数据回滚
                log.error("异常处理失败：{}", e.getMessage());
                e.printStackTrace();

            }
        }
    }
}
