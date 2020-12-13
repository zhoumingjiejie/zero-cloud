package com.github.icezerocat.zeroopenfeign.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 异常信息
 * CreateDate:  2020/12/8 14:03
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class ExceptionInfo {
    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 异常
     */
    private String exception;

    /**
     * 异常跟踪
     */
    private String trace;

    /**
     * 信息
     */
    private String message;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 错误（httStatus状态码对应消息）
     */
    private String error;

}
