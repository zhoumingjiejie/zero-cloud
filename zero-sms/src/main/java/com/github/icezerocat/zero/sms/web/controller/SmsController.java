package com.github.icezerocat.zero.sms.web.controller;

import com.github.icezerocat.zero.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 消息控制器
 * CreateDate:  2021/8/16 16:41
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("sms")
@RequiredArgsConstructor
public class SmsController {

    final private SmsService smsService;

    /**
     * 发送消息
     */
    @GetMapping("sendSms")
    public void sendSms() {
        this.smsService.sendSms();
    }
}
