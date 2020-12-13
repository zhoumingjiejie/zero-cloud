package com.github.icezerocat.zerocommon.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 异常信息详细数据
 * CreateDate:  2020/12/13 16:21
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
class ExceptionInfoData implements Serializable {
    /**
     * 时间戳
     */
    private Date timestamp;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 错误
     */
    private String error;

    /**
     * 信息
     */
    private String message;

    /**
     * 异常跟踪
     */
    private String trace;

    /**
     * 请求路径
     */
    private String path;
}
