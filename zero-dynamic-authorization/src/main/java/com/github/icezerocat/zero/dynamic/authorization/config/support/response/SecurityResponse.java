package com.github.icezerocat.zero.dynamic.authorization.config.support.response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 安全框架返回对象
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-03 10:39
 */
@Getter
@Setter
public final class SecurityResponse {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String FIELD_TIMESTAMP = "timestamp";

    public static final String FIELD_HTTP_STATUS = "status";

    public static final String FIELD_MESSAGE = "message";

    public static final String FIELD_DATA = "data";

    @JSONField(serialize = false)
    public static final Object PLAIN_OBJECT = new Object();

    /**
     * {@link HttpStatus}
     */
    @JSONField(name = "status", defaultValue = "200", ordinal = 1)
    private int httpStatus;

    /**
     * 时间戳
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss", ordinal = 2)
    private LocalDateTime timestamp;

    /**
     * 消息
     */
    @JSONField(ordinal = 3)
    private String message;

    /**
     * 数据体
     */
    @JSONField(defaultValue = "{}", ordinal = 4)
    private Object data;

    /**
     * Description: 序列化
     *
     * @return 序列化后的 JSON 字符串: { timestamp: '', status: '', message: '', data: {} }
     */
    @Override
    public String toString() {
        // 使用 LinkedHashMap 保证序列化顺序
        return new JSONObject(new LinkedHashMap<>(4))
                .fluentPut(FIELD_TIMESTAMP, Optional.ofNullable(this.timestamp).orElse(LocalDateTime.now()).format(DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.CHINA)))
                .fluentPut(FIELD_HTTP_STATUS, this.httpStatus)
                .fluentPut(FIELD_MESSAGE, this.message)
                .fluentPut(FIELD_DATA, Optional.ofNullable(data).orElse(PLAIN_OBJECT))
                .toString();
    }

    public Map<String, String> toMap() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<>(4);
        map.put(FIELD_TIMESTAMP, Optional.ofNullable(this.timestamp).orElse(LocalDateTime.now()).format(DateTimeFormatter.ofPattern(TIME_PATTERN, Locale.CHINA)));
        map.put(FIELD_HTTP_STATUS, String.valueOf(this.httpStatus));
        map.put(FIELD_MESSAGE, this.message);
        map.put(FIELD_DATA, JSON.toJSONString(Optional.ofNullable(data).orElse(PLAIN_OBJECT)));
        return map;
    }

    /**
     * 构造器
     */
    @SuppressWarnings("unused")
    public static final class Builder {

        private final SecurityResponse securityResponse = new SecurityResponse();

        /**
         * Description: 获取构造器
         *
         * @return c.c.d.s.s.o.a.c.authorization.server.domain.dto.SecurityResponse.Builder
         * @author LiKe
         * @date 2020-06-03 12:51:15
         */
        public static Builder of() {
            return new Builder();
        }

        public Builder httpStatus(HttpStatus httpStatus) {
            securityResponse.httpStatus = Optional.ofNullable(httpStatus).orElse(HttpStatus.OK).value();
            return this;
        }

        public Builder timestamp(LocalDateTime localDateTime) {
            securityResponse.timestamp = Optional.ofNullable(localDateTime).orElse(LocalDateTime.now());
            return this;
        }

        public Builder message(String message) {
            securityResponse.message = Optional.ofNullable(message).orElse(StringUtils.EMPTY);
            return this;
        }

        public Builder data(Object data) {
            securityResponse.data = Optional.ofNullable(data).orElse(PLAIN_OBJECT);
            return this;
        }

        /**
         * Description: 返回构建的 {@link SecurityResponse} 实例
         *
         * @return c.c.d.s.s.o.a.c.authorization.server.domain.dto.SecurityResponse
         * @author LiKe
         * @date 2020-06-03 13:19:32
         */
        public SecurityResponse build() {
            if (Objects.isNull(securityResponse.timestamp)) {
                securityResponse.timestamp = LocalDateTime.now();
            }
            return securityResponse;
        }

    }

}
