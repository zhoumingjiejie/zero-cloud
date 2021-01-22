package com.github.icezerocat.oauth2.service.impl;

import com.github.icezerocat.oauth2.service.SecurityOauthService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: oauth权限服务实现类
 * CreateDate:  2021/1/11 16:43
 *
 * @author zero
 * @version 1.0
 */
@Service("securityOauthService")
public class SecurityOauthServiceImpl implements SecurityOauthService {
    @Override
    public Map<String, List<ConfigAttribute>> loadDataSource() {
        //从数据库读取配置，url对应的角色
        return new HashMap<String, List<ConfigAttribute>>() {{
            //put("/ddptYyz/getDdptYyzAll","ROLE_ADMIN");
            put("/user", SecurityConfig.createList("ROLE_ADMIN", "ROLE_USER1"));
            put("/user/test3", SecurityConfig.createList("ROLE_ADMIN", "ROLE_USER1"));
        }};
    }
}
