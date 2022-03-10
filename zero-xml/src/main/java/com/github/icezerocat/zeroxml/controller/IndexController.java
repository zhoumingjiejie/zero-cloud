package com.github.icezerocat.zeroxml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description: 首页控制器
 * CreateDate:  2021/11/4 9:58
 *
 * @author zero
 * @version 1.0
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "redirect:/swagger-ui.html";
    }
}
