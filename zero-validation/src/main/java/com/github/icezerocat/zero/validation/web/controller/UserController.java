package com.github.icezerocat.zero.validation.web.controller;

import com.github.icezerocat.zero.validation.annotations.FormBodyParam;
import com.github.icezerocat.zero.validation.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("resolverParam")
    public Object resolverParam(@Valid User user, @RequestParam String say) {
        log.debug("say:{}", say);
        log.debug("扩展@RequestParam注解:{}", user);
        return user;
    }

    @PostMapping("formBody")
    public Object formBody(@FormBodyParam User user) {
        log.debug("扩展@RequestParam注解:{}", user);
        return user;
    }

    @PostMapping("paramPost")
    public String paramPost(@RequestParam String say) {
        log.debug("paramPost:{}", say);
        return say;
    }

    @GetMapping("paramGet/{say}")
    public String paramGet(@PathVariable(name = "say") String say,@RequestParam String hello){
        log.debug("paramGet:{}", say);
        log.debug("paramGet:{}", hello);
        return say;
    }
}
