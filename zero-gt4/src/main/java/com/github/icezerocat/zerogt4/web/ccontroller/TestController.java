package com.github.icezerocat.zerogt4.web.ccontroller;

import cn.gov.chinatax.gt4.yfwjcpt.sdk.client.YfwjcptHttpClient;
import cn.gov.chinatax.gt4.yfwjcpt.sdk.client.req.YfwjcptHttpRequest;
import cn.gov.chinatax.gt4.yfwjcpt.sdk.client.res.YfwjcptHttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 测试控制器
 * CreateDate:  2022/6/13 17:02
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping
public class TestController {

    /**
     * 测试内外网链路
     *
     * @return 请求结果
     */
    @RequestMapping("test")
    public String test() {
        YfwjcptHttpRequest yfwjcptHttpRequest = new YfwjcptHttpRequest();
        yfwjcptHttpRequest.setCustomUrl("/test/say");
        yfwjcptHttpRequest.setService("tsf");
        YfwjcptHttpResponse yfwjcptHttpResponse =  YfwjcptHttpClient.doGet(yfwjcptHttpRequest);
        log.debug("云服务集成平台请求返回结果：{}", JSONObject.toJSONString(yfwjcptHttpResponse));
        return yfwjcptHttpResponse.toString();
    }
}
