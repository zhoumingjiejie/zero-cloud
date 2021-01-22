package com.github.icezerocat.authorization.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: User 实体对象
 * CreateDate:  2021/1/12 20:35
 *
 * @author zero
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class User {

    /**
     * 用户 ID
     */
    private String id;

    /**
     * 用户密码 (加密过的)
     */
    private String password;

    /**
     * 用户名
     */
    private String username;
}
