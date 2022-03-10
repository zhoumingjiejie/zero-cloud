package com.github.icezerocat.zero.lock.service;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * Description: 分布式锁
 * CreateDate:  2020/12/17 16:03
 *
 * @author zero
 * @version 1.0
 */
public interface DistributedLocker {
    /**
     * 拿不到lock就不罢休，不然线程就一直block
     *
     * @param lockKey key
     * @return 锁
     */
    RLock lock(String lockKey);

    /**
     * 时间锁,超时自动解
     *
     * @param lockKey key
     * @param timeout 时间(单位为秒)
     * @return 锁
     */
    RLock lock(String lockKey, long timeout);

    /**
     * 时间锁,自定义时间单位
     *
     * @param lockKey key
     * @param unit    时间单位
     * @param timeout 时间数值
     * @return 锁
     */
    RLock lock(String lockKey, TimeUnit unit, long timeout);

    /**
     * 尝试获取锁:(公平锁)最多等待10秒，锁定后经过lockTime秒后自动解锁
     *
     * @param lockKey   key
     * @param unit      时间单位
     * @param waitTime  等待时间
     * @param leaseTime 获取锁后有效时间
     * @return 锁获取结果
     */
    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    /**
     * 释放锁
     *
     * @param lockKey key（String）
     */
    void unlock(String lockKey);

    /**
     * 释放锁
     *
     * @param lock lock（锁对象）
     */
    void unlock(RLock lock);

    /**
     * 时间锁（秒）
     *
     * @param lock      锁
     * @param leaseTime 租期时间
     * @return 锁
     */
    RLock lock(RLock lock, long leaseTime);

    /**
     * 时间锁
     *
     * @param lock    锁
     * @param unit    时间单位
     * @param timeout 时间值
     * @return 锁
     */
    RLock lock(RLock lock, TimeUnit unit, long timeout);

    /**
     * 尝试获取锁
     *
     * @param lock      锁
     * @param unit      时间单位
     * @param waitTime  等待时间
     * @param leaseTime 租期时间
     * @return 锁
     */
    boolean tryLock(RLock lock, TimeUnit unit, long waitTime, long leaseTime);
}
