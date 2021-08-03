package com.github.icezerocat.zero.dynamic.authorization.config.support.client;

import com.github.icezerocat.component.redisson.utils.RedisKey;
import com.github.icezerocat.component.redisson.utils.RedisUtil;
import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientDto;
import com.github.icezerocat.zero.dynamic.authorization.repository.client.ClientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description: 自定义客户端明细{@link ClientDetailsService}
 * CreateDate:  2021/7/22 19:26
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service("customClientDetailsService")
public class CustomClientDetailsServiceImpl implements ClientDetailsService {
    /**
     * client-details 缓存前缀
     */
    private static final String CACHE_PREFIX_CLIENT_DETAILS = "client-details";

    final private ClientMapper clientMapper;

    /**
     * Description: 从数据库中获取已经注册过的客户端信息<br>
     * Details: 该方法会在整个认证过程中被多次调用, 所以应该缓存. 和令牌的过期时间一致.
     *
     * @param clientId 客户端 ID
     * @see ClientDetailsService#loadClientByClientId(String)
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        log.debug("About to produce ClientDetails with client-id: {}", clientId);

        // ~ 对于 Authorization Server 来说, 请求它的客户端只可能是:
        //   1. 第一方客户端的前端和第一方客户端的后端: TODO 需要定义 client-id 和 client-secret
        //   2. 第三方客户端 (信息位于权限体系表里边)
        // -------------------------------------------------------------------------------------------------------------

        // =============================================================================================================

        // ~ 第三方客户端, 从权限体系表 / 缓存中获取
        // -------------------------------------------------------------------------------------------------------------

        final RedisKey cacheKey = RedisKey.builder().prefix(CACHE_PREFIX_CLIENT_DETAILS).suffix(clientId).build();

        // 先从缓存中获取 ClientDto
        ClientDto clientDto = RedisUtil.getInstance().getValue(cacheKey, ClientDto.class);
        // 如果缓存中没有, 从数据库查询并置入缓存
        if (Objects.isNull(clientDto)) {
            clientDto = this.clientMapper.getClient(clientId);

            if (Objects.isNull(clientDto)) {
                throw new ClientRegistrationException(String.format("客户端 %s 尚未注册!", clientId));
            }

            RedisUtil.getInstance().setValue(cacheKey, clientDto, clientDto.getAccessTokenValidity());
        }

        return new CustomClientDetails(clientDto);
    }
}
