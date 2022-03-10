package com.github.icezerocat.zero.jdbc.entity;

import com.github.icezerocat.zero.jdbc.builder.annotation.Column;
import com.github.icezerocat.zero.jdbc.builder.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:  事项表
 * Date: 2022-01-25 17:19:12
 *
 * @author 0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_guide_item")
public class TGuideItem implements Serializable {

    private static final long serialVersionUID = 1165708295809391578L;

    /**
     * 业务事项ID
     */
    @Column("ITEM_ID")
    private String itemId;
    public static final String ITEM_ID = "ITEM_ID";

    /**
     * 事项名称
     */
    @Column("ITEM_NAME")
    private String itemName;
    public static final String ITEM_NAME = "ITEM_NAME";

    /**
     * 业务事项代码
     */
    @Column("ITEM_CODE")
    private String itemCode;
    public static final String ITEM_CODE = "ITEM_CODE";

    /**
     * 办理模式 限时办结 即时办结
     */
    @Column("ITEM_MODE")
    private String itemMode;
    public static final String ITEM_MODE = "ITEM_MODE";

    /**
     * 限时办结时间
     */
    @Column("ITEM_TIMELIMIT")
    private String itemTimelimit;
    public static final String ITEM_TIMELIMIT = "ITEM_TIMELIMIT";

}
