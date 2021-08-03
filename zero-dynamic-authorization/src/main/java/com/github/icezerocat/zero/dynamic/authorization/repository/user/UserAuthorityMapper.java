package com.github.icezerocat.zero.dynamic.authorization.repository.user;

import com.github.icezerocat.zero.dynamic.authorization.domain.user.UserAuthorityResourceAddressMapping;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 用户端职权相关 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-07-31 15:22
 */
@Repository
public interface UserAuthorityMapper {

    /**
     * Description: 组织用户端职权 - 资源地址的映射关系<br>
     * Details: 资源地址格式为: 端点名@资源 ID
     *
     * @return java.util.Set<c.c.d.s.s.o.da.rs.configuration.support.authority.domain.dto.UserAuthorityResourceAddressMapping>
     * 持有用户端职权名称和资源地址映射对象的集合
     * @author LiKe
     * @date 2020-08-03 13:48:03
     */
    @Select(" select " +
            "   ua.NAME as user_authority_name, " +
            "   concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) as resource_address " +
            " from USER_AUTHORITY ua " +
            " left join MAPPING_USER_AUTHORITY_TO_RESOURCE mcatr on mcatr.USER_AUTHORITY_ID = ua.ID " +
            " left join RESOURCE r on r.ID = mcatr.RESOURCE_ID " +
            " where concat(r.ENDPOINT, '@', r.RESOURCE_SERVER_ID) is not null "
    )
    Set<UserAuthorityResourceAddressMapping> composeUserAuthorityResourceAddressMapping();

}
