package com.github.icezerocat.authorization.security;

import com.github.icezerocat.authorization.model.client.CustomClientDetails;
import com.github.icezerocat.authorization.model.dto.ClientDTO;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

/**
 * Description: 客户端信息服务实现类
 * CreateDate:  2021/1/13 10:03
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Service("clientDetailsServiceImpl")
public class ClientDetailsServiceImpl implements ClientDetailsService {
    /**
     * Description: 从数据库中获取已经注册过的客户端信息<br>
     * Details: 该方法会在整个认证过程中被多次调用, 所以应该缓存. 和令牌的过期时间一致.
     *
     * @param clientId 客户端 ID
     * @see ClientDetailsService#loadClientByClientId(String)
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        //TODO 数据库获取数据，设置缓存
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setResourceIds(Sets.newHashSet("resource-server"));
        clientDTO.setAuthorities(Sets.newHashSet("ROLE_ADMIN"));
        clientDTO.setId("client");
        clientDTO.setClientSecret("$2a$10$BM/gcUwVjYpKgNXQERlr8eq1cm/CdXmVAZuB1Qqg5lXu/hizWG1zi");
        clientDTO.setScope("user,admin,app");
        clientDTO.setAuthorizedGrantType("authorization_code,implicit,password,client_credentials,refresh_token");
        clientDTO.setRedirectUri("http://www.baidu.com");
        clientDTO.setAccessTokenValidity(99999);
        clientDTO.setRefreshTokenValidity(99999);
        clientDTO.setAutoApprove(true);
        clientDTO.setDescription("客户端描述");
        log.debug("客户端数据：{}", clientDTO);
        return new CustomClientDetails(clientDTO);
    }
}
