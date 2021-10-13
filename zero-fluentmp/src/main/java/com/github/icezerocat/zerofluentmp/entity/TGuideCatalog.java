package com.github.icezerocat.zerofluentmp.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Description:  目录主题
 * Date: 2021-10-13 09:33:55 
 * 
 * @author 0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FluentMybatis
public class TGuideCatalog implements IEntity {

	private static final long serialVersionUID = 8224603934354098120L;

	/**
	 * 目录ID
	 */
	@TableField("CATALOG_ID")
	private String catalogId;
	public static final String CATALOG_ID = "CATALOG_ID";

	/**
	 * 目录编码
	 */
	@TableField("CATALOG_CODE")
	private String catalogCode;
	public static final String CATALOG_CODE = "CATALOG_CODE";

	/**
	 * 目录名称
	 */
	@TableField("CATALOG_NAME")
	private String catalogName;
	public static final String CATALOG_NAME = "CATALOG_NAME";

	/**
	 * 目录简称
	 */
	@TableField("CATALOG_JC")
	private String catalogJc;
	public static final String CATALOG_JC = "CATALOG_JC";

	/**
	 * 上级目录编码
	 */
	@TableField("PARENT_CODE")
	private String parentCode;
	public static final String PARENT_CODE = "PARENT_CODE";

	@TableField("IORDER")
	private BigDecimal iorder;
	public static final String IORDER = "IORDER";

	@Override
	public Class<? extends IEntity> entityClass() {
		return TGuideCatalog.class;
	}

}
