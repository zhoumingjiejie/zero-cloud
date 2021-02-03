package com.github.icezerocat.clientorder.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:  订单
 * Date: 2021-01-28 21:22:57
 *
 * @author 0.0
 */
@Data
@NoArgsConstructor
@TableName("mall_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 9210172410425928786L;

    @TableId
    @TableField("id")
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 数量
     */
    @TableField("count")
    private Long count;

    /**
     * 金额
     */
    @TableField("money")
    private BigDecimal money;

    /**
     * 订单状态：0：创建中；1：已完结
     */
    @TableField("status")
    private Long status;

}
