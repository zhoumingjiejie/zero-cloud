package com.github.icezerocat.zero.activemq2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Description: 发布控制器
 * CreateDate:  2021/8/26 11:49
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("publish")
@RequiredArgsConstructor
public class PublishController {

    final private JmsMessagingTemplate jmsMessagingTemplate;
    final private Queue queue;
    final private Topic topic;

    /**
     * 发送队列消息
     *
     * @return 发送结果
     */
    @RequestMapping("/queue")
    public String queue() {
        for (int i = 0; i < 10; i++) {
            jmsMessagingTemplate.convertAndSend(this.queue, "queue-" + i);
        }
        return "queue 发送成功";
    }

    /**
     * 出队列消息回调
     *
     * @param msg 消息
     */
    @JmsListener(destination = "out.queue")
    public void consumerMsg(String msg) {
        System.out.println(msg);
    }

    /**
     * 发送主题消息
     *
     * @return 发送结果
     */
    @RequestMapping("/topic")
    public String topic() {
        for (int i = 0; i < 10; i++) {
            jmsMessagingTemplate.convertAndSend(this.topic, "topic-" + i);
        }
        return "topic 发送成功";
    }
}
