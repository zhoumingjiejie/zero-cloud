package com.github.icezerocat.zero.dynamic.authorization.repository.user;

import com.github.icezerocat.zero.dynamic.authorization.domain.user.UserDto;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * USER Mapper
 *
 * @author LiKe
 * @version 1.0.0
 * @date 2020-06-22 09:50
 */
@Repository
public interface UserMapper {

    String TABLE_NAME = "USER";

    /**
     * Description: 通过用户 ID 获取用户对象
     *
     * @param username 用户 ID
     * @return c.c.d.s.s.o.csd.as.domain.user.dto.UserDTO
     * @author LiKe
     * @date 2020-06-22 10:49:39
     */
    @Select("SELECT u.* from " + TABLE_NAME + " u where u.USERNAME = #{username}")
    @Results({
            @Result(id = true, property = "id", column = "ID"),
            @Result(property = "password", column = "PASSWORD"),
            @Result(property = "username", column = "USERNAME"),
            @Result(property = "authorities", column = "ID", javaType = Set.class,
                    many = @Many(
                            select = "com.github.icezerocat.zero.dynamic.authorization.repository.user.map.MappingUserToUserAuthorityMapper.getUserAuthorities"
                    )
            )
    })
    UserDto getUser(String username);

}
