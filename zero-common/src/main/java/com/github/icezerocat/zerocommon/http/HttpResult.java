package com.github.icezerocat.zerocommon.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * HTTP结果封装
 *
 * @author 0.0.0
 */
@SuppressWarnings("all")
@Data
@AllArgsConstructor
public class HttpResult<T> implements Serializable {

    final private Integer code;
    final private String msg;
    final private T data;
    final private Long count;
    final private HttpStatus httpStatus;

    private HttpResult(Build<T> build) {
        this.code = build.code;
        this.msg = build.msg;
        this.data = build.data;
        this.count = build.count;
        this.httpStatus = build.httpStatus;
    }

    public static class Build<T> {
        private Integer code;
        private String msg;
        private T data;
        private Long count;
        private HttpStatus httpStatus;

        public static <T> Build<T> getInstance() {
            return new Build<>();
        }

        /**
         * 设置 code
         *
         * @param code code
         * @return build
         */
        public Build<T> setCode(Integer code) {
            this.code = code;
            return this;
        }

        /**
         * 设置信息
         *
         * @param msg msg
         * @return build
         */
        public Build<T> setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        /**
         * 设置数据
         *
         * @param data data
         * @return build
         */
        public Build<T> setData(T data) {
            this.data = data;
            return this;
        }

        /**
         * 设置总数
         *
         * @param count 总数
         * @return build
         */
        public Build<T> setCount(Long count) {
            this.count = count;
            return this;
        }

        /**
         * 设置状态（包括code和message）
         *
         * @param httpStatus
         * @return
         */
        public Build<T> setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            if (StringUtils.isEmpty(this.code)) {
                this.code = httpStatus.value();
            }
            if (StringUtils.isEmpty(this.msg)) {
                this.msg = httpStatus.getReasonPhrase();
            }
            return this;
        }

        /**
         * 完成构建
         *
         * @return httpResult
         */
        public HttpResult<T> complete() {
            return new HttpResult<>(this);
        }
    }

    public static <T> HttpResult<T> error() {
        return HttpResult.Build.<T>getInstance().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg("未知异常，请联系管理员").complete();
    }

    public static <T> HttpResult<T> error(String msg) {
        return HttpResult.Build.<T>getInstance().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setMsg(msg).complete();
    }

    private static <T> HttpResult<T> error(Integer code, String msg) {
        return HttpResult.Build.<T>getInstance().setCode(code).setMsg(msg).complete();
    }

    public static <T> HttpResult<T> ok(String msg) {
        return HttpResult.Build.<T>getInstance().setCode(HttpStatus.OK.value()).setMsg(msg).complete();
    }

    public static <T> HttpResult<T> ok(T data) {
        return HttpResult.Build.<T>getInstance().setHttpStatus(HttpStatus.OK).setData(data).complete();
    }

    public static <T> HttpResult<T> ok() {
        return Build.<T>getInstance().setHttpStatus(HttpStatus.OK).complete();
    }

}
