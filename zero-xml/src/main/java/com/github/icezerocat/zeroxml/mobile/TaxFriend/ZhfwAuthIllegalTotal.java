package com.github.icezerocat.zeroxml.mobile.TaxFriend;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Description:  TODO
 * Date: 2021-11-02 11:19:07
 *
 * @author  0.0
 */
@Data
@ApiModel("违法情况合计")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class ZhfwAuthIllegalTotal implements Serializable {

	private static final long serialVersionUID =  7220388719081854522L;

	/**
	 * 纳税人体检报告ID
	 */
	@ApiModelProperty("纳税人体检报告ID")
	private String taxpatreReportId;

	/**
	 * 总数
	 */
	private int zs;

	/**
	 * 已处理数
	 */
	private int ycl;

	/**
	 * 未处理数
	 */
	private int wcls;

}
