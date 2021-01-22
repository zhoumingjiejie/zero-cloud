package com.github.icezerocat.authorization.model.client;

import com.github.icezerocat.authorization.model.dto.ClientDTO;
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
 * Description: 自定义客户端详细信息
 * CreateDate:  2021/1/13 10:09
 *
 * @author zero
 * @version 1.0
 */
public class CustomClientDetails implements ClientDetails {
    private final ClientDTO clientDTO;

    public CustomClientDetails(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    private static Set<String> composeFrom(String raw) {
        return Arrays.stream(StringUtils.split(raw, ","))
                .map(StringUtils::trimToEmpty).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    @Override
    public String getClientId() {
        return clientDTO.getId();
    }

    @Override
    public Set<String> getResourceIds() {
        return clientDTO.getResourceIds();
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientDTO.getClientSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return clientDTO.getScopes();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return composeFrom(clientDTO.getAuthorizedGrantType());
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return composeFrom(clientDTO.getRedirectUri());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return clientDTO.getAuthorities().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return clientDTO.getAccessTokenValidity();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return clientDTO.getRefreshTokenValidity();
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return clientDTO.isAutoApprove();
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
