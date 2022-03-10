package com.github.icezerocat.zeroxml.controller;

import com.github.icezerocat.zeroxml.mobile.TaxFriend.Wfwjxx;
import com.github.icezerocat.zeroxml.mobile.TaxFriend.ZhfwAuthIllegalTotal;
import com.github.icezerocat.zeroxml.mobile.TaxFriend.ZhfwAuthIllegalViolation;
import github.com.icezerocat.component.common.utils.Dom4jUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Description: xml控制器
 * CreateDate:  2021/9/15 13:38
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Api(tags = "税友体检报告")
@RestController
@RequestMapping("sy")
public class SyTestController {

    @RequestMapping("test")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "请求地址", required = true, dataType = "string", paramType = "query", example = "http://93.12.116.25:8070/servicebus/api/v1/service/"),
            @ApiImplicitParam(name = "nsrsbh", value = "纳税人识别号", dataType = "string", paramType = "query", example = "91620103MA72DY5318")
    })
    public Object test(@RequestParam(required = false) String url, @RequestParam(required = false) String nsrsbh) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        try {
            // 默认：七里河税友
            url = StringUtils.isNotBlank(url) ? url : "http://93.12.116.25:8070/servicebus/api/v1/service/";
            // 默认：广东睿盟计算机科技有限公司兰州分公司
            nsrsbh = StringUtils.isNotBlank(nsrsbh) ? nsrsbh : "91620103MA72DY5318";
            result.put("文件读取", "进行中");
            //根据纳税人识别号获取税友纳税人体检报告数据
            Document document = getDocument("webService/TaxFriendRequestNsrtj.xml");
            result.put("文件读取", "完成");
            result.put("document", documentEscapeTextToString(document));
            result.put("文档解析", "进行中");
            Element body = Dom4jUtil.getChildElement(document.getRootElement(), "body");
            String text = "<![CDATA[[\"" + nsrsbh + "\"]]]>";
            body.setText(text);
            String dataToString = documentEscapeTextToString(document);
            log.debug("报文：{}", dataToString);
            result.put("报文请求", dataToString);
            //请求税友获取数据
//            String soap = WebServiceUtil.doPostSoap(url, dataToString);
            String soap = "<service><head><tran_id>GS.CX.WSCX.NSRXX.NSRWFWJXX</tran_id><channel_id>qlhzhswdtClient</channel_id><tran_seq>789722e0f6e14a3babd95c302df022ba</tran_seq><returnMsg></returnMsg><rtnCode>0000</rtnCode></head><body><![CDATA[{\"jylsh\":\"13cd3f40c4ef46b186072df7696bf8b8\",\"resultMap\":{},\"success\":true,\"value\":[]}]]></body></service>";
            log.debug("报文请求结果:{}", soap);
            result.put("报文请求结果", soap);
            result.put("报文请求结果解析", "进行中");
            boolean jx = this.jx(soap);
            result.put("返回数据解析结果", String.valueOf(jx));
            result.put("完成", "true");
        } catch (Exception e) {
            log.debug("税友接口请求异常：{}", e.getMessage(), e);
            result.put("税友接口请求异常", e.getMessage());
            result.put("结果失败", "failed");
        }
        return result;
    }

    /**
     * 解析逻辑
     *
     * @param soap 报文结果
     * @return 解析结果
     */
    private boolean jx(String soap) {
        Document reqDocument = Dom4jUtil.getXmlByString(soap);
        //Document reqDocument = Dom4jUtil.getDocument("/webService/requestDemo.xml"); //当前为模拟数据
        Element reqBody = Dom4jUtil.getChildElement(reqDocument.getRootElement(), "body");
        String text1 = reqBody.getText();
        log.debug("json转化：{}", text1);
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(text1);
        log.debug("json转化成功：{}", jsonObject);
        String success = jsonObject.getString("success");
        if ("true".equals(success)) {
            String value = jsonObject.getString("value");
            log.debug("Wfwjxx-json转化：{}", text1);
            List<Wfwjxx> wfwjxxList = com.alibaba.fastjson.JSONObject.parseArray(value, Wfwjxx.class);
            log.debug("Wfwjxx-json结果：{}", wfwjxxList);
            //封装成智慧服务格式数据
            ZhfwAuthIllegalTotal zhfwAuthIllegalTotal = new ZhfwAuthIllegalTotal();
            List<ZhfwAuthIllegalViolation> zhfwAuthIllegalViolationList = new ArrayList<>();
            //定义违法违规数据
            int ycl = 0;
            int wcls = 0;
            if (!CollectionUtils.isEmpty(wfwjxxList)) {
                for (Wfwjxx wfwjxx : wfwjxxList) {
                    if ("处理完毕".equals(wfwjxx.getWfxwclzt())) {
                        ycl = ycl + 1;
                    } else {
                        wcls = wcls + 1;
                    }
                    ZhfwAuthIllegalViolation zhfwAuthIllegalViolation = convertObject(wfwjxx, ZhfwAuthIllegalViolation.class);
                    zhfwAuthIllegalViolation.setClzt(wfwjxx.getWfxwclzt());
                    zhfwAuthIllegalViolation.setWfss(wfwjxx.getWfxwlx());
                    zhfwAuthIllegalViolationList.add(zhfwAuthIllegalViolation);
                }
                zhfwAuthIllegalTotal.setZs(wfwjxxList.size());
                zhfwAuthIllegalTotal.setYcl(ycl);
                zhfwAuthIllegalTotal.setWcls(wcls);
                log.debug("解析数据成功1-违法情况合计：{}", zhfwAuthIllegalTotal);
                log.debug("解析数据成功2-违法违纪信息：{}", zhfwAuthIllegalViolationList);
                return true;
            }
        }
        return false;
    }

    /**
     * DOM4j的Document对象转为XML报文串,不转义文本
     *
     * @param document 文档
     * @return 经过解析后的xml字符串
     */
    private static String documentEscapeTextToString(Document document) {
        StringWriter stringWriter = new StringWriter();
        // 获得格式化输出流
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 写文件流
        XMLWriter xmlWriter = new XMLWriter(stringWriter, format);
        xmlWriter.setEscapeText(false);
        try {
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringWriter.toString();
    }

    /**
     * 根据路径获取xml文本
     *
     * @param path 文件路径
     * @return 文档
     */
    private static Document getDocument(String path) {
        if (null == path) {
            return null;
        }
        Document document;
        InputStream resourceAsStream = SyTestController.class.getClassLoader().getResourceAsStream(path);
        try {
            SAXReader reader = new SAXReader();
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            document = reader.read(resourceAsStream);
        } catch (Exception e) {
            log.error("加载xml文件失败：{}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return document;
    }

    public static <T, V> V convertObject(T source, Class<V> targetClass, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        V instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
        org.springframework.beans.BeanUtils.copyProperties(source, instance, ignoreProperties);
        return instance;
    }
}
