package com.github.icezerocat.zero.dynamic.authorization.repository.client;

import com.github.icezerocat.zero.dynamic.authorization.domain.client.ClientAuthorityResourceAddressMapping;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 客户端职权相关 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-07-31 11:15
 */
@Repository
public interface ClientAuthorityMapper {

    /**
     * Description: 组织客户端职权 - 资源地址的映射关系<br>
     * Details: 资源地址格式为: 端点名@资源 ID
     *
     * @return java.util.Set<c.c.d.s.s.o.da.rs.configuration.support.authority.domain.dto.ClientAuthorityResourceAddressDTO>
     * 持有客户端职权名称和资源地址映射对象的集合
     * @author LiKe
     * @date 2020-08-03 10:17:57
     */
    @Select(" select " +
            " 	ca.NAME as client_authority_name, " +
            " 	concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) as resource_address " +
            " from CLIENT_AUTHORITY ca " +
            " left join MAPPING_CLIENT_AUTHORITY_TO_RESOURCE mcatr on mcatr.CLIENT_AUTHORITY_ID = ca.ID " +
            " left join RESOURCE r on r.ID = mcatr.RESOURCE_ID " +
            " where concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) is not null "
    )
    Set<ClientAuthorityResourceAddressMapping> composeClientAuthorityResourceAddressMapping();

}
