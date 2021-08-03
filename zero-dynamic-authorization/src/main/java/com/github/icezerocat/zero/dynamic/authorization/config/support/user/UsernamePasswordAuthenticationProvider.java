package com.github.icezerocat.zero.dynamic.authorization.config.support.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description: 身份认证
 * CreateDate:  2021/10:53
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Component("usernamePasswordAuthenticationProvider")
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    /**
     * 密码编码
     * {@link com.github.icezerocat.zero.dynamic.authorization.config.AuthorizationServerConfig#passwordEncoder()}
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * 用户信息
     */
    @Resource(name = "customUserDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //用户账号密码认证
        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (this.passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("用户密码错误!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.debug("支持的认证方式：{}", authentication);
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
