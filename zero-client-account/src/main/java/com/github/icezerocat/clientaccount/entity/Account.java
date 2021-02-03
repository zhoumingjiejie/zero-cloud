package com.github.icezerocat.clientaccount.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:  账号
 * Date: 2021-01-28 21:41:02
 *
 * @author 0.0
 */
@Data
@NoArgsConstructor
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 8062146942176661082L;

    /**
     * id
     */
    @TableId
    @TableField("id")
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 总额度
     */
    @TableField("total")
    private BigDecimal total;

    /**
     * 已用余额
     */
    @TableField("used")
    private BigDecimal used;

    /**
     * 剩余可用额度
     */
    @TableField("residue")
    private BigDecimal residue;

}
