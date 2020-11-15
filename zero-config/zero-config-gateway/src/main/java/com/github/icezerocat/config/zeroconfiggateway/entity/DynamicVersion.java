package com.github.icezerocat.config.zeroconfiggateway.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:  路由版本表
 * Date: 2020-11-13 13:21:10 
 * 
 * @author  0.0
 */
@Data
@TableName(value = "dynamic_version")
public class DynamicVersion  implements Serializable {

	private static final long serialVersionUID =  4067218799045198965L;

	/**
	 * 主键、自动增长、版本号
	 */
	@TableId
   	@TableField(value = "id" )
	private Long id;

   	@TableField(value = "create_time" )
	private Date createTime;
}
