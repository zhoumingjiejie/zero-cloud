package com.github.icezerocat.authorization.model.entity;

import com.github.icezerocat.authorization.config.WebSecurityConfig;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 客户端实体对象
 * CreateDate:  2021/1/12 20:37
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class Client {

    /**
     * 客户端 ID(主键)
     */
    private String id;

    /**
     * 客户端 Secret (经过 {@link WebSecurityConfig#passwordEncoder()} 加密的)
     */
    private String clientSecret;

    /**
     * 客户端 Scope (英文逗号分隔)
     */
    private String scope;

    /**
     * 授权方式. 可能的值有: authorization_code/implicit/password/client_credentials/refresh_token 的其中一种或多种 (英文逗号分隔)
     */
    private String authorizedGrantType;

    /**
     * 重定向地址, 当授权方式是 authorization_code 时有效. 如果有多个, 按英文逗号分隔.
     */
    private String redirectUri;

    /**
     * access-token 生命周期 (秒)
     */
    private Integer accessTokenValidity;

    /**
     * refresh-token 生命周期 (秒)
     */
    private Integer refreshTokenValidity;

    /**
     * 是否自动允许. 如果为 true, 则不需要用户手动允许
     */
    private boolean autoApprove;

    /**
     * 客户端描述
     */
    private String description;
}
