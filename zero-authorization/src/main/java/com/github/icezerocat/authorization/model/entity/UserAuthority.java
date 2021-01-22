package com.github.icezerocat.authorization.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 用户职权
 * CreateDate:  2021/1/12 20:42
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class UserAuthority {
    /**
     * 用户职权 ID
     */
    private String id;

    /**
     * 用户职权名称
     */
    private String name;

    /**
     * 用户职权描述
     */
    private String description;
}
