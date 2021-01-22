package com.github.icezerocat.authorization.security;

import com.github.icezerocat.authorization.model.dto.UserDTO;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Description: 用户信息匹对服务
 * CreateDate:  2021/1/12 20:47
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO 通过用户名获取用户信息，判断用户是否存在
        UserDTO user = new UserDTO();
        user.setAuthorities(Sets.newHashSet("ROLE_ADMIN"));
        user.setId("1");
        //123456
        user.setPassword("$2a$10$BM/gcUwVjYpKgNXQERlr8eq1cm/CdXmVAZuB1Qqg5lXu/hizWG1zi");
        user.setUsername("admin");
        log.debug("用户信息数据：{}", user);
        return new CustomUserDetails(
                Optional.ofNullable(user).orElseThrow(() -> new UsernameNotFoundException(String.format("无此用户 (%s)!", username)))
        );
    }
}
