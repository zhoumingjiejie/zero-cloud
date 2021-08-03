package com.github.icezerocat.zero.dynamic.authorization.boot;

import com.github.icezerocat.component.redisson.utils.RedisKey;
import com.github.icezerocat.component.redisson.utils.RedisUtil;
import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientAccessScopeResourceAddressMapping;
import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientAuthorityResourceAddressMapping;
import com.github.icezerocat.zero.dynamic.authorization.domain.user.UserAuthorityResourceAddressMapping;
import com.github.icezerocat.zero.dynamic.authorization.repository.client.ClientAccessScopeMapper;
import com.github.icezerocat.zero.dynamic.authorization.repository.client.ClientAuthorityMapper;
import com.github.icezerocat.zero.dynamic.authorization.repository.user.UserAuthorityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * Description: 资源地址元数据初始化<br>
 * Details: 授权服务器启动的时候, 从数据源里加载访问控制香瓜你的元数据, 并放入缓存. 供资源服务器调用
 * CreateDate:  2021/7/23 11:32
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Order(1)
@Component
public class ResourceAddressMetadataInitializer implements ApplicationRunner {

    /**
     * metadata.resource-address 缓存前缀
     */
    private static final String CACHE_PREFIX_METADATA_RESOURCE_ADDRESS = "metadata:resource-address";

    @Resource
    private ClientAccessScopeMapper clientAccessScopeMapper;
    @Resource
    private ClientAuthorityMapper clientAuthorityMapper;
    @Resource
    private UserAuthorityMapper userAuthorityMapper;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Resource address metadata initializing ...");
        // 初始化客户端访问范围
        this.initClientAccessScope();
        // 初始化客户端访问权限
        this.initClientAuthority();
        // 初始化用户权限
        this.initUserAuthority();
        log.info("Resource address metadata initialized.");
    }

    /**
     * 初始化客户端访问范围
     */
    private void initClientAccessScope() {
        RedisUtil.getInstance().hash().putAll(
                RedisKey.builder().prefix(CACHE_PREFIX_METADATA_RESOURCE_ADDRESS).suffix(ClientAccessScopeResourceAddressMapping.CACHE_SUFFIX).build(),
                clientAccessScopeMapper.composeClientAccessScopeResourceAddressMapping().stream().collect(Collectors.toMap(
                        ClientAccessScopeResourceAddressMapping::getClientAccessScopeName,
                        ClientAccessScopeResourceAddressMapping::getResourceAddress
                ))
        );
        log.debug("==============================================================================");
        log.debug("|            Metadata: ClientAccessScope - ResourceAddress Cached            |");
        log.debug("==============================================================================");
    }

    /**
     * 初始化客户端访问权限
     */
    private void initClientAuthority() {
        RedisUtil.getInstance().hash().putAll(
                RedisKey.builder().prefix(CACHE_PREFIX_METADATA_RESOURCE_ADDRESS).suffix(ClientAuthorityResourceAddressMapping.CACHE_PREFIX).build(),
                clientAuthorityMapper.composeClientAuthorityResourceAddressMapping().stream().collect(Collectors.toMap(
                        ClientAuthorityResourceAddressMapping::getClientAuthorityName,
                        ClientAuthorityResourceAddressMapping::getResourceAddress
                ))
        );
        log.debug("==============================================================================");
        log.debug("|             Metadata: ClientAuthority - ResourceAddress Cached             |");
        log.debug("==============================================================================");
    }

    /**
     * 初始化用户权限
     */
    private void initUserAuthority() {
        RedisUtil.getInstance().hash().putAll(
                RedisKey.builder().prefix(CACHE_PREFIX_METADATA_RESOURCE_ADDRESS).suffix(UserAuthorityResourceAddressMapping.CACHE_PREFIX).build(),
                userAuthorityMapper.composeUserAuthorityResourceAddressMapping().stream().collect(Collectors.toMap(
                        UserAuthorityResourceAddressMapping::getUserAuthorityName,
                        UserAuthorityResourceAddressMapping::getResourceAddress
                ))
        );
        log.debug("==============================================================================");
        log.debug("|              Metadata: UserAuthority - ResourceAddress Cached              |");
        log.debug("==============================================================================");
    }

}
