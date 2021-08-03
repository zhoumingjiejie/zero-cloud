package com.github.icezerocat.zero.dynamic.authorization.repository.client;

import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientDto;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 自定义 {@link ClientDetails} 的 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-15 13:05
 */
@Repository
public interface ClientMapper {

    String TABLE_NAME = "CLIENT";

    /**
     * Description: 通过客户端 ID 获取客户端
     *
     * @param clientId 客户端 ID
     * @return {@link ClientDto}
     * @author LiKe
     * @date 2020-06-15 13:17:53
     */
    @Select("SELECT c.* FROM " + TABLE_NAME + " c WHERE c.ID = #{clientId}")
    @Results({
            @Result(id = true, property = "id", column = "ID"),
            @Result(property = "clientSecret", column = "CLIENT_SECRET"),
            @Result(property = "scopes", column = "ID", javaType = Set.class,
                    many = @Many(
                            select = "com.github.icezerocat.zero.dynamic.authorization.repository.client.map.MappingClientToClientAccessScopeMapper.queryClientAccessScopes"
                    )
            ),
            @Result(property = "authorizedGrantType", column = "AUTHORIZED_GRANT_TYPE"),
            @Result(property = "redirectUri", column = "REDIRECT_URI"),
            @Result(property = "accessTokenValidity", column = "ACCESS_TOKEN_VALIDITY"),
            @Result(property = "refreshTokenValidity", column = "REFRESH_TOKEN_VALIDITY"),
            @Result(property = "autoApprove", column = "AUTO_APPROVE"),
            @Result(property = "description", column = "DESCRIPTION"),
            @Result(property = "resourceIds", column = "ID", javaType = Set.class,
                    many = @Many(
                            select = "com.github.icezerocat.zero.dynamic.authorization.repository.client.map.MappingClientToResourceServerMapper.queryResourceServerIds"
                    )
            ),
            @Result(property = "authorities", column = "ID", javaType = Set.class,
                    many = @Many(
                            select = "com.github.icezerocat.zero.dynamic.authorization.repository.client.map.MappingClientToClientAuthorityMapper.queryClientAuthorities"
                    )
            )
    })
    ClientDto getClient(String clientId);

}
