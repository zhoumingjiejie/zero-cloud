package com.github.icezerocat.clientstorage.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

/**
 * Description:  库存
 * Date: 2021-01-28 10:46:18
 *
 * @author 0.0
 */
@Data
@NoArgsConstructor
@TableName("storage")
public class Storage implements Serializable {

    private static final long serialVersionUID = 6593427950079264090L;

    @TableId
    @TableField("id")
    private Long id;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 总库存
     */
    @TableField("total")
    private Long total;

    /**
     * 已用库存
     */
    @TableField("used")
    private Long used;

    /**
     * 剩余库存
     */
    @TableField("residue")
    private Long residue;

}
