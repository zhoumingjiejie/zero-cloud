package com.github.icezerocat.zero.dynamic.authorization.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: 用户
 * CreateDate:  2021/7/22 18:02
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

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

    /**
     * 当前用户对应的职权（角色） {@link UserAuthority}s
     */
    private Set<String> authorities = new HashSet<>();
}
