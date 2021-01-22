package com.github.icezerocat.authorization.model.dto;

import com.github.icezerocat.authorization.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Description: Dto
 * CreateDate:  2021/1/12 20:40
 * {@link User}
 *
 * @author zero
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserDTO extends User {
    /**
     * 当前用户对应的职权
     * {@link com.github.icezerocat.authorization.model.entity.UserAuthority}
     */
    private Set<String> authorities;
}
