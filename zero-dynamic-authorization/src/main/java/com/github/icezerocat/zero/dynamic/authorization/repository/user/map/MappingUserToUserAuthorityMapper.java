package com.github.icezerocat.zero.dynamic.authorization.repository.user.map;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 用户到用户职权的映射关系 Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-22 11:18
 */
@Repository
@SuppressWarnings("unused")
public interface MappingUserToUserAuthorityMapper {

    String TABLE_NAME = "MAPPING_USER_TO_USER_AUTHORITY";

    /**
     * Description: 查询用户所对应的职权
     *
     * @param userId 用户 ID
     * @return java.util.Set<java.lang.String>
     * @author LiKe
     * @date 2020-06-22 11:24:59
     */
    @Select("SELECT ua.NAME " +
            "FROM " + TABLE_NAME + " mutua LEFT JOIN USER_AUTHORITY ua ON ua.ID = mutua.USER_AUTHORITY_ID " +
            "WHERE mutua.USER_ID = #{userId}")
    Set<String> getUserAuthorities(String userId);

}
