package com.github.icezerocat.zero.oauth2.password.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 控制器
 * CreateDate:  2021/11/16 11:17
 *
 * @author zero
 * @version 1.0
 */
@RestController
public class UserController {

    @GetMapping("/api/userInfo")
    public ResponseEntity<String> getUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername() + "@qq.com";

        return ResponseEntity.ok(email);
    }

    @RequestMapping("/api/hi")
    public String say(String name) {
        return "hi , " + name;
    }
}
