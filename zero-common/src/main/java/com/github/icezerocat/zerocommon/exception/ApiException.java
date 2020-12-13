package com.github.icezerocat.zerocommon.exception;

import java.io.Serializable;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.githup.icezerocat.core.exception.ZeroException]
 * Description: 自定义异常
 * CreateDate:  2020/4/20 22:58
 *
 * @author 0.0.0
 * @version 1.0
 */
@SuppressWarnings("unused")
public class ApiException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -7230273470995591933L;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 状态码
     */
    private Integer status = 500;

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


    public ApiException(String message) {
        super(message);
        this.message = message;
    }

    public ApiException(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public ApiException(String message, Throwable cause, int status) {
        super(message, cause);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", exception='" + exception + '\'' +
                ", trace='" + trace + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
