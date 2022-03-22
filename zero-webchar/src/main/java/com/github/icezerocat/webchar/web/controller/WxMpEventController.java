package com.github.icezerocat.webchar.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 事件控制器
 * CreateDate:  2022/3/18 17:46
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("event/{appid}")
public class WxMpEventController {

    final private WxMpService wxMpService;
    final private WxMpMessageRouter wxMpMessageRouter;

    /**
     * 接口配置信息调用接口(GET)
     *
     * @param appid   公众号id
     * @param request 请求
     * @return 随机字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String doGet(@PathVariable String appid, HttpServletRequest request) {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            log.info("signature " + signature + "  timestamp " + timestamp + " nonce " + nonce
                    + " echostr " + echostr);
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        final WxMpService wxService = this.wxMpService.switchoverTo(appid);
        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实!", appid));
        }

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            log.info("weixin get success...." + echostr);
            return echostr;
        } else {
            log.error("weixin get failed....");
            return "weixin get failed....";
        }
    }

    /**
     * 关注，取关，客服，菜单等调用接口(POST)
     *
     * @param appid   公众号id
     * @param request 请求
     * @return 随机字符串
     */
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String doPost(@PathVariable String appid, @RequestBody String requestBody, HttpServletRequest request) {
        log.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // openid
        String openid = request.getParameter("openid");
        // encType--可空
        String encType = request.getParameter("encType");
        // msgSignature--可空
        String msgSignature = request.getParameter("msgSignature");
        log.debug("消息内容：{}", requestBody);
        log.debug("{}\t{}\t{}\t{}", signature, timestamp, nonce, StringUtils.join(openid, "-", encType, "-", msgSignature));

        final WxMpService wxService = this.wxMpService.switchoverTo(appid);

        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            log.info(
                    "\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                            + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                    openid, signature, encType, msgSignature, timestamp, nonce, requestBody);
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求!");
        }

        String out = null;
        if (StringUtils.isBlank(encType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage, appid);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
                    wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage, appid);
            if (outMessage == null) {
                return "";
            }
            out = outMessage.toEncryptedXml(wxService.getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    /**
     * 调用路由进行消息处理
     *
     * @param message 消息
     * @param appid   应用ID
     * @return WxMpXmlOutMessage
     */
    private WxMpXmlOutMessage route(WxMpXmlMessage message, String appid) {
        try {
            return this.wxMpMessageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常!", e);
        }
        return null;
    }

}
