package com.github.icezerocat.component.redisson.config;

import com.github.icezerocat.component.redisson.service.DistributedLocker;
import com.github.icezerocat.component.redisson.service.impl.DistributedLockerImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by zmj
 * On 2019/12/4.
 *
 * @author 0.0.0
 */
@Slf4j
@Configuration
public class RedisComponentConfig {
    /**
     * redis序列化配置
     *
     * @param redisConnectionFactory redis链接工厂
     * @return redisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.debug("stater:redis加载");
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        // key序列化
        redisTemplate.setKeySerializer(stringSerializer);
        // value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash key序列化
        redisTemplate.setHashKeySerializer(stringSerializer);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    /**
     * redis开启事务
     *
     * @param redisConnectionFactory redis链接工厂
     * @return StringRedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory);
        // description 开启redis事务（仅支持单机，不支持cluster）
        template.setEnableTransactionSupport(true);
        return template;
    }

    /**
     * 分布式锁
     *
     * @param redissonClient redis客户端
     * @return 分布式锁
     */
    @Bean
    @ConditionalOnMissingBean(name = "distributedLocker")
    public DistributedLocker distributedLocker(RedissonClient redissonClient) {
        return new DistributedLockerImpl(redissonClient);
    }
}
