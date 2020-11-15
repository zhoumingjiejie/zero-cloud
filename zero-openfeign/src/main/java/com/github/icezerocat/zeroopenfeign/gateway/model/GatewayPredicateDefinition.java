package com.github.icezerocat.zeroopenfeign.gateway.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description: 路由断言模型
 * CreateDate:  2020/11/11 12:42
 *
 * @author zero
 * @version 1.0
 */
@Data
public class GatewayPredicateDefinition {
    /**
     * 断言对应的Name
     */
    private String name;
    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<>();
}
