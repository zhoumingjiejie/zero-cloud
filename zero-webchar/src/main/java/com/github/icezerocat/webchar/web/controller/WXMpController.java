package com.github.icezerocat.webchar.web.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Description: 微信公众号登录控制器
 * CreateDate:  2021/10/25 17:45
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("mp")
public class WXMpController {

    @Resource
    private WxMpService wxMpService;


    /**
     * String baseUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
     * "appid=xxxxxxxxxxx&" +
     * "redirect_uri=http%3a%2f%2fxxxxxxxxxxx%2fsell%2fwechat&" +
     * "response_type=code&scope=snsapi_userinfo&state=chuai#wechat_redirect";
     * <p>
     * 1、引导用户进入授权页面同意授权，获取code
     *
     * @return 重定向地址
     */
    @RequestMapping("/wechat/authorize")
    public String getWxMpCode(String returnUrl, String state) {
        log.debug("state验证是否来自微信重定向的请求:{}", state);
        String baseUrl = this.wxMpService.getOAuth2Service().buildAuthorizationUrl(
                returnUrl,
                WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                state);
        log.info("baseUrl打印：{}", baseUrl);
        return "redirect:" + baseUrl;
    }

    /**
     * 获取用户信息 openid 、accessToken等
     *
     * @param code   授权码
     * @param userId 用户id
     * @return 重定向页面
     */
    @RequestMapping("/wechat")
    public String getUser(String code, @RequestParam(required = false) String userId) {
        log.debug("userId:{}", userId);
        WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
        try {
            accessToken = this.wxMpService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo wxMpUser = this.wxMpService.getOAuth2Service().getUserInfo(accessToken, "zh_CN");
            log.debug("信息爆破：{}", wxMpUser);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openId = accessToken.getOpenId();
        log.info("授权成功：" + openId);
        log.debug("{}", accessToken);
        return "redirect:/index.html";
    }
}
