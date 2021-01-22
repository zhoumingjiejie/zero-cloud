package com.github.icezerocat.authorization.security;

import com.github.icezerocat.authorization.model.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Description: 自定义用户信息
 * CreateDate:  2021/1/12 20:44
 * {@link UserDetails}
 *
 * @author zero
 * @version 1.0
 */
public class CustomUserDetails implements UserDetails {
    /**
     * {@link UserDTO}
     */
    private final UserDTO userDTO;

    CustomUserDetails(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.userDTO.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userDTO.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
