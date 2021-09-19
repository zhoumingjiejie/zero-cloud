package com.github.icezerocat.webchar.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: 微信回调控制器
 * 接收微信事件推送接口
 * get 和 post 方法访问路径一定要一致。
 * CreateDate:  2021/9/6 10:31
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("weixinCallBack")
public class WeiXinCallbackController {

    /**
     * 验证地址的正确性
     * 一定要使用【get】方式
     *
     * @param request 请求
     * @return 接收到的信息
     */
    @GetMapping(value = "/callBack")
    public String callBack(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.debug("{}\t{}\t{}\t{}", signature, timestamp, nonce, echostr);
        return echostr;
    }

    /**
     * 接收消息推送
     * 一定要使用【post】方式
     * 貌似有些接口给的是json，有些接口给的是xml
     *
     * @param request 请求
     * @param resp    响应
     * @param s       接口结果数据
     * @return 返回
     */
    @PostMapping(value = "/callBack")
    public Boolean callBack(HttpServletRequest request, HttpServletResponse resp, @RequestBody String s) {
        // xml  格式的 或者 json 格式的字符串
        System.out.println(s);
        // 业务逻辑处理
        // ....
        //  返回给微信，果然没有返回微信会重试几次
        return true;
    }
}
