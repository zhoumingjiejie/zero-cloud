package com.github.icezerocat.zero.dynamic.authorization.repository.client.map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 客户端到访问范围的映射关系 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-07-30 20:25
 */
@Repository
@SuppressWarnings("unused")
public interface MappingClientToClientAccessScopeMapper {

    String TABLE_NAME = "MAPPING_CLIENT_TO_CLIENT_ACCESS_SCOPE";

    /**
     * Description: 查询客户端访问范围
     *
     * @param clientId 客户端 ID
     * @return java.util.Set<java.lang.String>
     * @author LiKe
     * @date 2020-07-31 09:16:42
     */
    @Select(" SELECT cas.SCOPE " +
            " FROM " + TABLE_NAME + " mctcas " +
            " 	LEFT JOIN CLIENT_ACCESS_SCOPE cas ON mctcas.CLIENT_ACCESS_SCOPE_ID = cas.ID " +
            " WHERE mctcas.CLIENT_ID = #{clientId} "
    )
    Set<String> queryClientAccessScopes(String clientId);

}
