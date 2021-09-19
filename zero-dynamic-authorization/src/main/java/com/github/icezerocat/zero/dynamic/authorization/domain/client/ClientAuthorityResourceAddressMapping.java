package com.github.icezerocat.zero.dynamic.authorization.domain.client;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 客户端职权到资源地址的映射 DTO<br>
 * Details: 资源地址格式: {@code {endpoint}@{resource-server-id}}
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-07-31 10:56
 */
@Data
@NoArgsConstructor
public class ClientAuthorityResourceAddressMapping {

    /**
     * 客户端职权名称
     */
    private String clientAuthorityName;

    /**
     * 资源地址
     */
    private String resourceAddress;

}
