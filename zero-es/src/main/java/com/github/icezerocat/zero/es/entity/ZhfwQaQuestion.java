package com.github.icezerocat.zero.es.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import com.github.icezerocat.zero.es.constant.EsAnalyzerConstant;
import com.github.icezerocat.zero.es.constant.EsIndexConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Description:  问题库
 * Date: 2022-02-14 14:48:31
 *
 * @author 0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FluentMybatis(dbType = DbType.ORACLE)
@Document(indexName = EsIndexConstant.QA_QUESTION_INDEX)
public class ZhfwQaQuestion implements IEntity {

    private static final long serialVersionUID = 8204268429061712146L;

    /**
     * ID
     */
    @TableId
    private String id;
    public static final String ID = "ID";

    /**
     * 类型(JCWD：基础问答、DLWD：多轮问答等)
     */
    @TableField("TYPE")
    private String type;
    public static final String TYPE = "TYPE";

    /**
     * 问题
     */
    @Field(type = FieldType.Text, searchAnalyzer = EsAnalyzerConstant.IK_SMART, analyzer = EsAnalyzerConstant.IK_SMART)
    @TableField("QUESTION")
    private String question;
    public static final String QUESTION = "QUESTION";

    /**
     * 删除标识（0：否，1：是，默认0）
     */
    @TableField("DELETE_FLAG")
    private String deleteFlag;
    public static final String DELETE_FLAG = "DELETE_FLAG";

    /**
     * 创建日期
     */
    @TableField("CREATE_DATE")
    private Date createDate;
    public static final String CREATE_DATE = "CREATE_DATE";

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;
    public static final String UPDATE_TIME = "UPDATE_TIME";

    /**
     * 权重(缺省1)
     */
    @TableField("WEIGHT")
    private String weight;
    public static final String WEIGHT = "WEIGHT";

    /**
     * 来源渠道
     */
    @TableField("CHANNEL")
    private String channel;
    public static final String CHANNEL = "CHANNEL";

    /**
     * 热点标识符（0：否，1：是，默认0）
     */
    @TableField("HOT_FLAG")
    private String hotFlag;
    public static final String HOT_FLAG = "HOT_FLAG";
}
