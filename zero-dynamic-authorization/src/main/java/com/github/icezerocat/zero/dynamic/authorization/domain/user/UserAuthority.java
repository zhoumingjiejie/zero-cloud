package com.github.icezerocat.zero.dynamic.authorization.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 用户职权
 * CreateDate:  2021/7/22 18:04
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority implements Serializable {
    /**
     * 用户职权 ID
     */
    private String id;

    /**
     * 用户职权名称(角色)
     */
    private String name;

    /**
     * 用户职权描述
     */
    private String description;
}
