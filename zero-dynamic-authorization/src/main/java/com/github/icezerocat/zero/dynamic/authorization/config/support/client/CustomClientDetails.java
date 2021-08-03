package com.github.icezerocat.zero.dynamic.authorization.config.support.client;

import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientDto;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description: 自定义客户端明细
 * CreateDate:  2021/7/22 18:58
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
public class CustomClientDetails implements ClientDetails {

    private ClientDto clientDto;

    public CustomClientDetails(ClientDto clientDto) {
        this.clientDto = clientDto;
    }

    @Override
    public String getClientId() {
        return this.clientDto.getId();
    }

    @Override
    public Set<String> getResourceIds() {
        return this.clientDto.getResourceIds();
    }

    /**
     * 是否需要密钥
     *
     * @return true
     */
    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.clientDto.getClientSecret();
    }

    /**
     * 是否有权限范围
     *
     * @return true
     */
    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return this.clientDto.getScopes();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return composeFrom(this.clientDto.getAuthorizedGrantType());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return composeFrom(this.clientDto.getRedirectUri());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.clientDto.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.clientDto.getAccessTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.clientDto.getRefreshTokenValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.clientDto.isAutoApprove();
    }

    /**
     * 获取附加信息
     *
     * @return 信息map
     */
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    /**
     * 权限数据转Set
     *
     * @param raw 权限数据
     * @return set集合
     */
    private static Set<String> composeFrom(String raw) {
        return Arrays.stream(StringUtils.split(raw, ","))
                .map(StringUtils::trimToEmpty).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }
}
