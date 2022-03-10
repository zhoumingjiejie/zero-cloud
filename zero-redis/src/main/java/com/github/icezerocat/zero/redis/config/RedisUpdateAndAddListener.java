package com.github.icezerocat.zero.redis.config;

import lombok.Data;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

/**
 * Description: 新增或修改key监听
 * CreateDate:  2022/3/6 13:35
 *
 * @author zero
 * @version 1.0
 */
@Data
@Component
public class RedisUpdateAndAddListener implements MessageListener {
    /**
     * 监听的主题
     */
    private final PatternTopic topic = new PatternTopic("__keyevent@*__:set");

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topic = new String(pattern);
        String msg = new String(message.getBody());
        System.out.println("收到key更新或修改，消息主题是：" + topic + ",消息内容redis-key是：" + msg);
    }
}
