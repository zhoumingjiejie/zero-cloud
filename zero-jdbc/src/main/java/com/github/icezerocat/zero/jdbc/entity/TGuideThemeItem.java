package com.github.icezerocat.zero.jdbc.entity;

import com.github.icezerocat.zero.jdbc.builder.annotation.Column;
import com.github.icezerocat.zero.jdbc.builder.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:  主题事项关联表
 * Date: 2022-01-25 17:19:40
 *
 * @author 0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_guide_theme_item")
public class TGuideThemeItem implements Serializable {

	private static final long serialVersionUID = 2774742763673824277L;

	/**
	 * 主题ID
	 */
	@Column("THEME_ID")
	private String themeId;
	public static final String THEME_ID = "THEME_ID";

	/**
	 * 业务事项ID
	 */
	@Column("ITEM_ID")
	private String itemId;
	public static final String ITEM_ID = "ITEM_ID";

	/**
	 * 办理类型 是否必做
	 */
	@Column("ITEM_TYPE")
	private String itemType;
	public static final String ITEM_TYPE = "ITEM_TYPE";

}
