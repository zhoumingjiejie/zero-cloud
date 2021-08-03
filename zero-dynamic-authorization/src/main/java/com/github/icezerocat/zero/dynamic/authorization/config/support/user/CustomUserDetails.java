package com.github.icezerocat.zero.dynamic.authorization.config.support.user;

import com.github.icezerocat.zero.dynamic.authorization.domain.user.UserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Description: 自定义用户明细：用户生成token扩展用户信息
 * CreateDate:  2021/7/22 17:34
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
public class CustomUserDetails implements UserDetails {

    /**
     * 用户Dto
     */
    private UserDto userDto;

    public CustomUserDetails(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userDto.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.userDto.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userDto.getUsername();
    }

    /**
     * 账户是否未过期,过期无法验证
     *
     * @return 账号过期无法验证
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return true(锁定的用户无法进行身份验证)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return true（过期的凭据防止认证）
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return 启用
     */
    @Override
    public boolean isEnabled() {
        return false;
    }
}
