package com.github.icezerocat.zero.dynamic.authorization.repository.client;

import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientAccessScopeResourceAddressMapping;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 客户端访问访问相关 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-08-03 13:58
 */
@Repository
public interface ClientAccessScopeMapper {

    /**
     * Description: 组织客户端访问范围 - 资源地址的映射关系<br>
     * Details: 资源地址格式为: 端点名@资源 ID
     *
     * @return java.util.Set<c.c.d.s.s.o.da.rs.configuration.support.authority.repository.metadata.ClientAccessScopeResourceAddressMapping>
     * 持有客户端访问范围和资源地址映射对象的集合
     * @author LiKe
     * @date 2020-08-03 14:05:00
     */
    @Select(" SELECT " +
            "   cas.SCOPE as client_access_scope_name, " +
            "   concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) as resource_address " +
            " FROM " +
            " CLIENT_ACCESS_SCOPE cas " +
            " LEFT JOIN MAPPING_CLIENT_ACCESS_SCOPE_TO_RESOURCE mapping ON mapping.CLIENT_ACCESS_SCOPE_ID = cas.ID " +
            " LEFT JOIN RESOURCE r on r.ID = mapping.RESOURCE_ID " +
            " where concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) is not null "
    )
    Set<ClientAccessScopeResourceAddressMapping> composeClientAccessScopeResourceAddressMapping();

}
