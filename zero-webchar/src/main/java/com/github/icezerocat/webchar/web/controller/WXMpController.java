package com.github.icezerocat.webchar.web.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 发送模板消息
     *
     * @return 发送结果
     * @throws WxErrorException 微信错误异常
     */
    @GetMapping("sendTemplate")
    @ResponseBody
    public String sendTemplate() throws WxErrorException {
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                .toUser("osmQI6968AXXkbGiOW5aHHKBrzbo")
                .templateId("3xwfM5Og16VIJNZgvJYYsm7AcGgS0olQdGqUapDVP4I")
                .url("https://www.baidu.com")
                .build();
        // 添加模板数据
        wxMpTemplateMessage.addData(new WxMpTemplateData("first", "您好", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword1", "zero", "#A9A9A9"))
                .addData(new WxMpTemplateData("keyword2", "136****8094", "#FF00FF"))
                .addData(new WxMpTemplateData("keyword3", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"), "#FF00FF"))
                .addData(new WxMpTemplateData("keyword4", "请注意已被包围", "#FF00FF"))
                .addData(new WxMpTemplateData("remark", "解决方案：逃", "#000000"));
        String result = this.wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        log.debug("发送模板信息：{}", result);
        return result;
    }

    /**
     * 获取微信用户信息
     *
     * @return 用户信息
     * @throws WxErrorException 微信异常
     */
    @GetMapping("getUserInfo")
    @ResponseBody
    public WxMpUser getUserInfo() throws WxErrorException {
        WxMpUser wxMpUser = this.wxMpService.getUserService().userInfo("osmQI6968AXXkbGiOW5aHHKBrzbo");
        log.debug("用户信息：{}", wxMpUser);
        return wxMpUser;
    }

    /**
     * 获取用户列表信息
     *
     * @return 用户列表信息
     * @throws WxErrorException 微信异常
     */
    @GetMapping("getUserInfoList")
    @ResponseBody
    public WxMpUserList getUserInfoList() throws WxErrorException {
        WxMpUserService userService = this.wxMpService.getUserService();
        String nextOpenId = "";
        int count = 1;
        while (true) {
            WxMpUserList wxMpUserList = userService.userList(nextOpenId);
            nextOpenId = wxMpUserList.getNextOpenid();
            log.debug("获取用户列表:{}", wxMpUserList);
            this.getUserInfo(wxMpUserList.getOpenids());
            if (wxMpUserList.getCount() == 0) {
                break;
            }
        }
        return null;
    }

    /**
     * 用户信息
     *
     * @param openIdList 公众号用户ID
     * @throws WxErrorException 微信异常
     */
    private void getUserInfo(List<String> openIdList) throws WxErrorException {
        //分页拉取，最多支持一次拉取100条
        int pageNum = 1;
        int pageSize = 100;
        int totalSize = openIdList.size();
        int totalPages = new BigDecimal((double) totalSize / (double) pageSize).setScale(0, BigDecimal.ROUND_UP).intValue();
        for (; pageNum <= totalPages; pageNum++) {
            int toIndex = (pageNum * pageSize) - 1;
            List<String> pageOpenIdList = openIdList.subList((pageNum - 1) * pageSize, Math.min(toIndex, totalSize));
            List<WxMpUser> wxMpUsers = this.wxMpService.getUserService().userInfoList(pageOpenIdList);
            log.debug("用户信息:{}", wxMpUsers);
        }
    }
}
