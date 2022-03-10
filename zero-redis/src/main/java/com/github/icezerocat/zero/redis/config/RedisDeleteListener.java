package com.github.icezerocat.zero.redis.config;

import lombok.Data;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

/**
 * Description: redis删除监听
 * CreateDate:  2022/3/6 13:40
 *
 * @author zero
 * @version 1.0
 */
@Data
@Component
public class RedisDeleteListener implements MessageListener {

    /**
     * 监听主题
     */
    private final PatternTopic topic = new PatternTopic("__keyevent@*__:del");

    /**
     * @param message 消息
     * @param pattern 主题
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(pattern);
        String msg = new String(message.getBody());
        System.out.println("收到key的删除，消息主题是：" + topic + ",消息内容redis-key是：" + msg);
    }
}
