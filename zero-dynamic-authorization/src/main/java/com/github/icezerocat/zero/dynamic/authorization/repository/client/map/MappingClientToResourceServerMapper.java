package com.github.icezerocat.zero.dynamic.authorization.repository.client.map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 客户端到资源服务器的映射关系 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-17 11:37
 */
@Repository
@SuppressWarnings("unused")
public interface MappingClientToResourceServerMapper {

    String TABLE_NAME = "MAPPING_CLIENT_TO_RESOURCE_SERVER";

    /**
     * Description: 查询匹配的资源服务器 ID
     *
     * @param clientId 客户端 ID
     * @return java.util.Set<java.lang.String>
     * @author LiKe
     * @date 2020-06-17 11:39:08
     */
    @Select("SELECT RESOURCE_SERVER_ID FROM " + TABLE_NAME + " WHERE CLIENT_ID = #{clientId}")
    Set<String> queryResourceServerIds(String clientId);

}
