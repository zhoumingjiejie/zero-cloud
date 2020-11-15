package com.github.icezerocat.config.zeroconfiggateway.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description:  网关路由规则
 * Date: 2020-11-13 13:21:17
 *
 * @author 0.0
 */
@Data
@TableName(value = "gateway_routes")
public class GatewayRoutes implements Serializable {

    private static final long serialVersionUID = 4499637268101513561L;

    @TableId
    @TableField(value = "id")
    private Long id;

    /**
     * 路由id
     */
    @TableField(value = "route_id")
    private String routeId;

    /**
     * 转发目标uri
     */
    @TableField(value = "route_uri")
    private String routeUri;

    /**
     * 路由执行顺序
     */
    @TableField(value = "route_order")
    private Long routeOrder;

    /**
     * 断言字符串集合，字符串结构：[{
     * "name":"Path",
     * "args":{
     * "pattern" : "/zy/**"
     * }
     * }]
     */
    @TableField(value = "predicates")
    private String predicates;

    /**
     * 过滤器字符串集合，字符串结构：{
     * "name":"StripPrefix",
     * "args":{
     * "_genkey_0":"1"
     * }
     * }
     */
    @TableField(value = "filters")
    private String filters;

    /**
     * 是否启用
     */
    @TableField(value = "is_ebl")
    private int isEbl = 0;

    /**
     * 是否删除
     */
    @TableField(value = "is_del")
    private int isDel = 0;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

}
