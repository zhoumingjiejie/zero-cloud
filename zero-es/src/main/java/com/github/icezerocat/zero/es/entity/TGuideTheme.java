package com.github.icezerocat.zero.es.entity;

import com.github.icezerocat.zero.es.constant.EsIndexConstant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * Description:  主题组织架构
 * Date: 2020-11-23 14:56:26
 *
 * @author 0.0
 */
@Data
@Document(indexName = EsIndexConstant.GUIDE_THEME_INDEX)
public class TGuideTheme implements Serializable {

    private static final long serialVersionUID = 4339213316520792725L;

    /**
     * 主题ID
     */
    @Id
    @Field(type = FieldType.Keyword)
    private String themeId;

    /**
     * 主题编码
     */
    private String themeCode;

    /**
     * 用户类型代码
     */
    @Field(type = FieldType.Text, fielddata = true)
    private String userType;

    /**
     * 主题名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String themeName;

    /**
     * 主题简称
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String themeJc;

    /**
     * 主题事项概述
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String themeSummary;

    /**
     * 地市特有主题
     */

    private String belongMuId;

    /**
     * 是否启用0未开通1开通
     */

    private Integer isOpen;

    /**
     * 是否删除0否1是
     */
    private Integer isDelete;

}
