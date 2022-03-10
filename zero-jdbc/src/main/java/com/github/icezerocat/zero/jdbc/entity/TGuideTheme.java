package com.github.icezerocat.zero.jdbc.entity;

import com.github.icezerocat.zero.jdbc.builder.annotation.Column;
import com.github.icezerocat.zero.jdbc.builder.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:  主题表
 * Date: 2022-01-25 17:19:24
 *
 * @author 0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_guide_theme")
public class TGuideTheme implements Serializable {

	private static final long serialVersionUID = 7599166841124930049L;

	/**
	 * 主题ID
	 */
	@Column("theme_id")
	private String themeId;
	public static final String THEME_ID = "THEME_ID";

	/**
	 * 主题编码
	 */
	@Column("THEME_CODE")
	private String themeCode;
	public static final String THEME_CODE = "THEME_CODE";

	/**
	 * 用户类型代码
	 */
	@Column("USER_TYPE")
	private String userType;
	public static final String USER_TYPE = "USER_TYPE";

	/**
	 * 主题名称
	 */
	@Column("THEME_NAME")
	private String themeName;
	public static final String THEME_NAME = "THEME_NAME";

	/**
	 * 主题简称
	 */
	@Column("THEME_JC")
	private String themeJc;
	public static final String THEME_JC = "THEME_JC";

	/**
	 * 主题事项概述
	 */
	@Column("THEME_SUMMARY")
	private String themeSummary;
	public static final String THEME_SUMMARY = "THEME_SUMMARY";

	/**
	 * 地市特有主题
	 */
	@Column("BELONG_MU_ID")
	private String belongMuId;
	public static final String BELONG_MU_ID = "BELONG_MU_ID";

	/**
	 * 是否启用0未开通1开通
	 */
	@Column("IS_OPEN")
	private BigDecimal isOpen;
	public static final String IS_OPEN = "IS_OPEN";

	/**
	 * 是否删除0否1是
	 */
	@Column("IS_DELETE")
	private BigDecimal isDelete;
	public static final String IS_DELETE = "IS_DELETE";

}
