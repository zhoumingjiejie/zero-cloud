package com.github.icezerocat.zero.redis.constants;

import lombok.Builder;
import lombok.Data;

/**
 * Description: RedisKey
 * CreateDate:  2021/7/23 11:10
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
public class RedisKey {

    public static final String SEPARATOR = ":";

    /**
     * Redis key 的前缀
     */
    private String prefix;

    /**
     * Redis key 的内容
     */
    private String suffix;

    public String of() {
        return String.format("%s%s%s", prefix, SEPARATOR, suffix);
    }
}
