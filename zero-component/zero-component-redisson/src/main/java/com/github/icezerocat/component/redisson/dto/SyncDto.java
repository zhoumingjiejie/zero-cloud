package com.github.icezerocat.component.redisson.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 同步数据
 * CreateDate:  2020/12/22 13:56
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class SyncDto implements Serializable {
    private static final long serialVersionUID = 415197915086569719L;

    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * sql
     */
    private String sql;

    /**
     * value
     */
    private Object[] values;
}
