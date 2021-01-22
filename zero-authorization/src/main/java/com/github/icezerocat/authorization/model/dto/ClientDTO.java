package com.github.icezerocat.authorization.model.dto;

import com.github.icezerocat.authorization.model.entity.Client;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Description: Dto
 * CreateDate:  2021/1/12 20:38
 * {@link Client}
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientDTO extends Client {
    /**
     * 客户端可访问的资源 Id
     */
    private Set<String> resourceIds;

    /**
     * 客户端职权
     * {@link com.github.icezerocat.authorization.model.entity.UserAuthority}
     */
    private Set<String> authorities;

    /**
     * 客户端访问范围
     */
    private Set<String> scopes;

}
