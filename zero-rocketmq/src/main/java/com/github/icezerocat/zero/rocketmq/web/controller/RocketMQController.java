package com.github.icezerocat.zero.rocketmq.web.controller;

import com.github.icezerocat.zero.rocketmq.component.MQProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: rocketMq控制器
 * CreateDate:  2021/12/17 16:26
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/rocketmq")
@RequiredArgsConstructor
public class RocketMQController {

    final private MQProducerService mqProducerService;

    @GetMapping("/send")
    public void send() {
        mqProducerService.send("hello rocketMq");
    }

    @GetMapping("/sendTag")
    public SendResult sendTag() {
        SendResult sendResult = mqProducerService.sendTagMsg("带有tag的字符消息");
        log.debug("sendTag:{}", sendResult);
        return sendResult;
    }
}
