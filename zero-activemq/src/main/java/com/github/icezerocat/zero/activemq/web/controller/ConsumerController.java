package com.github.icezerocat.zero.activemq.web.controller;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 消费控制器
 * CreateDate:  2021/9/14 20:52
 *
 * @author zero
 * @version 1.0
 */
@RestController
public class ConsumerController {

    /**
     * 监听和读取消息
     *
     * @param message 消息
     */
    @JmsListener(destination = "publish.queue", containerFactory = "jmsListenerContainerQueue")
    public void readActiveQueue(String message) {
        System.out.println("接受到queue：" + message);
        //TODO something
    }

    /**
     * 监听和读取消息
     *
     * @param message 消息
     */
    @JmsListener(destination = "publish.topic", containerFactory = "jmsListenerContainerTopic")
    public void readActiveTopic(String message) {
        System.out.println("接受到topic：" + message);
        //TODO something
    }
}
