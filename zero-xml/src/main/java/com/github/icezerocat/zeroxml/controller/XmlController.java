package com.github.icezerocat.zeroxml.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.icezerocat.zeroxml.model.pojo.JsSl;
import com.github.icezerocat.zeroxml.model.pojo.Student;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import github.com.icezerocat.component.common.utils.Dom4jUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Description: xml控制器
 * CreateDate:  2021/9/15 13:38
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("xml")
public class XmlController {

    /**
     * dom4j解析xml
     */
    @GetMapping("dom4j")
    public void dom4j() {
        String xmlStr =
                "<note>" +
                        "  <to>Tove</to>" +
                        "  <from>Jani</from>" +
                        "  <heading>Reminder</heading>" +
                        "  <body>Don't forget me this weekend!</body>" +
                        "</note>";
        //Document document2 = Dom4jUtil.getXmlByString(xmlStr);
        Document document = Dom4jUtil.getDocument("data.xml");
        log.debug("报文：{}\t{}", document, Dom4jUtil.documentToString(document, "UTF-8"));
        List<Element> childElements = Dom4jUtil.getChildElements(document.getRootElement());
        Element toElement = Dom4jUtil.getChildElement(document.getRootElement(), "to");
        toElement.setText("\\<![CDATA[[\"520423199012142819\"]]]>");
        log.debug("to标签变更：{}", Dom4jUtil.documentToString(document, "GB23121"));
        log.debug("to标签变更：{}", document.asXML());
        if (!CollectionUtils.isEmpty(childElements)) {
            childElements.forEach(o -> {
                //TODO 问题1： http:进去、接收、返回； webService：进去报文格式要求、接收到什么格式解析、确认返回数据的要求
                log.debug("{}：{}", o.getName(), Dom4jUtil.getText(o));
            });
        }

    }

    /**
     * xstream解析xml
     *
     * @return 解析结果
     */
    @GetMapping("xs")
    public String xs() {
        Student student = new Student();
        student.setFirstName("Mahesh");
        student.setLastName("Parashar");
        student.setRollNo(1);
        student.setClassName("1st");
        Student.Address address = new Student.Address();
        address.setArea("H.No. 16/3, Preet Vihar.");
        address.setCity("Delhi");
        address.setState("Delhi");
        address.setCountry("India");
        student.setAddress(address);
        log.debug("原始toString：{}", address);

        // use xstream:1.4.11
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        //设置最低限制权限
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.autodetectAnnotations(true);

        String studentXml = xStream.toXML(student);
        log.debug(studentXml);
        log.debug(formatXml(studentXml));
        Student xmlToStudent = (Student) xStream.fromXML(studentXml);
        log.debug("xml转对象：{}", xmlToStudent);
        return xmlToStudent.toString();
    }

    @GetMapping("xmlToJson")
    public String xmlToJson() {
        String jsonXmlStr =
                "{\"result\":\"" +
                        "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>" +
                        "<taxML xsi:type=\\\"DZSWJSBJS10419Response\\\" xmlns=\\\"http://www.chinatax.gov.cn/dataspec/\\\" xmlns:xsi=\\\"http://www.w3.org/2001/XMLSchema-instance\\\">" +
                        "    <yhsl>0.0</yhsl>" +
                        "    <fdsl>0.0</fdsl>" +
                        "    <yssdl>1.0</yssdl>" +
                        "</taxML>\",\"reqId\":\"127375e1-5f27-4818-abe9-8e73c5749f7c\"}";
        JSONObject jsonObject = JSONObject.parseObject(jsonXmlStr);
        String result = jsonObject.getString("result");
        log.debug("中继器数据：{}", jsonObject);
        log.debug(result);
        Document xmlByString = Dom4jUtil.getXmlByString(result);
        String xmlResult = Dom4jUtil.documentToStringNoDeclaredHeader(xmlByString, "UTF-8");
        log.debug(xmlResult);
        XStream xStream = this.getXStream();
        JsSl jsSlObj = new JsSl();
        jsSlObj.setYhsl("1");
        jsSlObj.setFdsl("2");
        jsSlObj.setYssdl("3");
        log.debug("obj=>{}", xStream.toXML(jsSlObj));

        JsSl jsSl = (JsSl) xStream.fromXML(xmlResult);
        return jsSl.toString();
    }

    /**
     * 格式化xml字符串
     *
     * @param xml xml内容
     * @return 格式化结果-添加头部文件：<?xml version="1.0" encoding="UTF-8"?>
     */
    public static String formatXml(String xml) {
        try {
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
        } catch (Exception e) {
            return xml;
        }
    }


    /**
     * 获取xml操作对象
     *
     * @return 操作对象
     */
    private XStream getXStream() {
        // use xstream:1.4.11
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        //设置最低限制权限
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.autodetectAnnotations(true);
        return xStream;
    }

    /**
     * 使用最原始的javax.xml.parsers，标准的jdk api;XML转字符串
     *
     * @param document DOCUMENT为org.w3c.dom.Document
     * @return 字符串
     * @throws TransformerException 变压器异常
     */
    private String jdkApiXmlToStr(org.w3c.dom.Document document) throws TransformerException {
        //XML转字符串
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", "GB23121");//解决中文问题，试过用GBK不行
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.transform(new DOMSource(document), new StreamResult(bos));
        return bos.toString();
    }

    /**
     * 使用dom4j XML转字符串
     *
     * @param document DOCUMENT为org.dom4j.Document
     * @return 字符串
     */
    private String dom4jXmlToStr(Document document) {
        return document.asXML();
    }

    /**
     * JDOM的处理方式和第一种方法处理非常类似;XML转字符串
     *
     * @param document DOCUMENT为org.jdom.Document
     * @return 字符串
     * @throws IOException io异常
     */
    private String jdomXmlToStr(org.jdom2.Document document) throws IOException {
        //XML转字符串
        Format format = Format.getPrettyFormat();
        format.setEncoding("gb2312");//设置xml文件的字符为gb2312，解决中文问题
        XMLOutputter xmlout = new XMLOutputter(format);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        xmlout.output(document, bo);
        return bo.toString();
    }
}
