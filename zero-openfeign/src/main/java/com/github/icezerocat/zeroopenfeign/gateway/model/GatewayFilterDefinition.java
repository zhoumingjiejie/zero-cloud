package com.github.icezerocat.zeroopenfeign.gateway.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: 过滤器模型
 * CreateDate:  2020/11/11 12:42
 *
 * @author zero
 * @version 1.0
 */
@Data
public class GatewayFilterDefinition {
    /**
     * Filter Name
     */
    private String name;
    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}
