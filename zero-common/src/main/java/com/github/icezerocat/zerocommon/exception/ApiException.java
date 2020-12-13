package com.github.icezerocat.zerocommon.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.githup.icezerocat.core.exception.ZeroException]
 * Description: 自定义异常
 * CreateDate:  2020/4/20 22:58
 *
 * @author 0.0.0
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("unused")
public class ApiException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7230273470995591933L;

    /**
     * code
     */
    private int code;

    /**
     * 信息
     */
    private String message;

    /**
     * 总数
     */
    private Long count;

    /**
     * http状态
     */
    private String httpStatus;

    /**
     * 日期
     */
    private Date date;

    /**
     * 异常信息详细数据
     */
    private ExceptionInfoData data;


    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ApiException(String message, Throwable cause, int code) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }
}
