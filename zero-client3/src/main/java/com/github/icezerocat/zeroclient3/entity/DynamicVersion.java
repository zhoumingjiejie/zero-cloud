package com.github.icezerocat.zeroclient3.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:  路由器版本
 * Date: 2020-12-21 15:10:33
 *
 * @author 0.0
 */
@Data
@NoArgsConstructor
@TableName("dynamic_version")
@com.github.icezerocat.component.jdbctemplate.annotations.TableName("dynamic_version")
public class DynamicVersion implements Serializable {

    private static final long serialVersionUID = 2267069978940120212L;

    /**
     * 主键、自动增长、版本号
     */
    @TableId
    @com.github.icezerocat.component.jdbctemplate.annotations.TableId
    @TableField("id")
    private Long id;

    @TableField("create_time")
    private Date createTime;

}
