package com.github.icezerocat.zero.dynamic.authorization.config.support.user;

import com.github.icezerocat.zero.dynamic.authorization.repository.user.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Description: 自定义用户明细服务{@link UserDetailsService}
 * CreateDate:  2021/7/22 18:16
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("customUserDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    final private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("About to produce UserDetails with user-name: {}", username);
        return new CustomUserDetails(
                Optional.ofNullable(this.userMapper.getUser(username)).orElseThrow(() -> new UsernameNotFoundException(String.format("无此用户 (%s)!", username)))
        );
    }
}
