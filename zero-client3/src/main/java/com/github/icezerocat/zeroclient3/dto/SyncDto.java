package com.github.icezerocat.zeroclient3.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 微信同步dto
 * CreateDate:  2020/12/22 19:07
 *
 * @author zero
 * @version 1.0
 */
@Data
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
