package com.github.icezerocat.zero.dynamic.authorization.domain.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: 客户端dto
 * CreateDate:  2021/7/22 18:56
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {
    /**
     * 客户端 ID (主键)
     */
    private String id;

    /**
     * 客户端 Secret (经过 {@link org.springframework.security.crypto.password.PasswordEncoder#encode(CharSequence)} 加密的)
     */
    private String clientSecret;

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

    /**
     * 客户端可访问的资源 Id
     */
    private Set<String> resourceIds = new HashSet<>();

    /**
     * 客户端职权
     */
    private Set<String> authorities = new HashSet<>();

    /**
     * 客户端访问范围
     */
    private Set<String> scopes = new HashSet<>();
}
