package com.github.icezerocat.zero.dynamic.authorization.repository.client.map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 客户端到客户端职权的映射关系 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-17 12:52
 */
@Repository
@SuppressWarnings("unused")
public interface MappingClientToClientAuthorityMapper {

    String TABLE_NAME = "MAPPING_CLIENT_TO_CLIENT_AUTHORITY";

    /**
     * Description: 查询客户端职权 ID
     *
     * @param clientId 客户端 ID
     * @return java.util.Set<java.lang.String>
     * @author LiKe
     * @date 2020-06-17 12:57:33
     */
    @Select("SELECT ca.name " +
            "FROM " + TABLE_NAME + " mctca LEFT JOIN CLIENT_AUTHORITY ca ON ca.ID = mctca.CLIENT_AUTHORITY_ID " +
            "WHERE mctca.CLIENT_ID = #{clientId}")
    Set<String> queryClientAuthorities(String clientId);

}
