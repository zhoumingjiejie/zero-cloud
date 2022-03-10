package com.github.icezerocat.zero.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

/**
 * Description: redis监听配置
 * CreateDate:  2022/3/6 13:05
 *
 * @author zero
 * @version 1.0
 */
@Configuration
public class RedisListenerConfig {

    @Resource
    private RedisUpdateAndAddListener redisUpdateAndAddListener;

    @Resource
    private RedisDeleteListener redisDeleteListener;

    @Resource
    private RedisExpiredListener redisExpiredListener;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        //监听所有的key的set事件
        container.addMessageListener(this.redisUpdateAndAddListener, redisUpdateAndAddListener.getTopic());
        //监听所有key的删除事件
        container.addMessageListener(this.redisDeleteListener, this.redisDeleteListener.getTopic());
        //监听所有key的过期事件
        container.addMessageListener(this.redisExpiredListener, this.redisExpiredListener.getTopic());
        return container;
    }

}
