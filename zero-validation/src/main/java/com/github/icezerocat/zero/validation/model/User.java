package com.github.icezerocat.zero.validation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Description: 用户
 * CreateDate:  2021/7/28 9:36
 *
 * @author zero
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;
    /**
     * 密码
     */
    private String password;

    /**
     * 用户信息
     */
    @Valid
    private UserInfo userInfo;

    /**
     * 数组用户
     */
    private List<UserInfo> userInfos;
}
