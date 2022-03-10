package com.github.icezerocat.zero.lock.service.impl;

import com.github.icezerocat.zero.lock.service.DistributedLocker;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Description: 分布式锁实现类
 * CreateDate:  2020/12/17 17:30
 *
 * @author zero
 * @version 1.0
 */
@Service
public class DistributedLockerImpl implements DistributedLocker {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = this.redissonClient.getLock(lockKey);
        return this.lock(lock, leaseTime);
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = this.redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = this.redissonClient.getLock(lockKey);
        return this.tryLock(lock, unit, waitTime, leaseTime);
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = this.redissonClient.getLock(lockKey);
        this.unlock(lock);
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    @Override
    public RLock lock(RLock lock, long leaseTime) {
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(RLock lock, TimeUnit unit, long timeout) {
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(RLock lock, TimeUnit unit, long waitTime, long leaseTime) {
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

}
