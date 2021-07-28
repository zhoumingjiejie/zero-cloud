package com.github.icezerocat.zero.validation.web.controller;

import com.github.icezerocat.zero.validation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Description: 用户控制器
 * CreateDate:  2021/7/28 9:39
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("user")
@Validated
public class UserController {

    @PostMapping("check")
    public void check(@Valid @RequestBody User user) {
        log.debug("I will check user : {}", user);
    }
}
