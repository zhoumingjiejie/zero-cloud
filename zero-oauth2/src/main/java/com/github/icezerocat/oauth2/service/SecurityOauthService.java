package com.github.icezerocat.oauth2.service;

import org.springframework.security.access.ConfigAttribute;

import java.util.List;
import java.util.Map;

/**
 * Description: oauth权限服务
 * CreateDate:  2021/1/11 16:41
 *
 * @author zero
 * @version 1.0
 */
public interface SecurityOauthService {

    /**
     * 从数据库读取权限信息
     *
     * @return 权限信息
     */
    Map<String, List<ConfigAttribute>> loadDataSource();
}
