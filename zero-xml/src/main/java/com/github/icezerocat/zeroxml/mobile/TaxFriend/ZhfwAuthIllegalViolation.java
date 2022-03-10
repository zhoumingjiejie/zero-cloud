package com.github.icezerocat.zeroxml.mobile.TaxFriend;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Description:  TODO
 * Date: 2021-11-02 11:19:13
 *
 * @author  0.0
 */
@Data
@ApiModel("违法违纪信息")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ZhfwAuthIllegalViolation implements Serializable {

	private static final long serialVersionUID =  1721603102914567960L;

	/**
	 * 主键
	 */
	@ApiModelProperty("主键")
	private String id;

	/**
	 * 纳税人体检报告ID
	 */
	private String taxpatreReportId;

	/**
	 * 是否处理
	 */
	private String sfcl;

	private String wfss;

	/**
	 * 违法行为名称
	 */
	private String wfxwmc;

	/**
	 * 登记日期
	 */
	private Date djrq;

	/**
	 * 处理状态
	 */
	private String clzt;

	/**
	 * 处罚金额
	 */
	private String cfje;

	/**
	 * 缴款期限
	 */
	private String jkqx;

	/**
	 * 登记序号
	 */
	private String djxh;

	/**
	 * 起始时间
	 */
	private Date sssqq;

	/**
	 * 截至时间
	 */
	private Date sssqz;

   	private String wfxwlx;

}
