package com.github.icezerocat.zeroap.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 视图控制器
 * CreateDate:  2020/11/16 22:52
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("/")
public class ViewController {

    /**
     * 首页
     *
     * @return 首页
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 打开templates页面
     *
     * @param path 页面路径（可选）
     * @param name html文件名
     * @return 返回html
     */
    @RequestMapping("view")
    public String view(@RequestParam(required = false, defaultValue = "") String path, @RequestParam String name) {
        final String l = "/";
        if (name.indexOf(l) != 0 && path.lastIndexOf(l) != path.length() - 1) {
            name = l + name;
        }
        name = name.replace(".html", "");
        return StringUtils.isEmpty(path) ? name : (path + name);
    }
}
