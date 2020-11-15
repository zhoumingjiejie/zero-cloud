package com.github.icezerocat.config.zeroconfiggateway.web.controller;

import com.github.icezerocat.config.zeroconfiggateway.entity.DynamicVersion;
import com.github.icezerocat.config.zeroconfiggateway.mapper.DynamicVersionMapper;
import com.github.icezerocat.config.zeroconfiggateway.service.DynamicVersionService;
import com.github.icezerocat.zerocore.constants.RedisKey;
import com.github.icezerocat.zerocore.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Description: 动态路由版本控制器
 * CreateDate:  2020/11/14 1:30
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("version")
public class DynamicVersionController {

    @Resource
    private DynamicVersionMapper dynamicVersionMapper;
    private final DynamicVersionService dynamicVersionService;

    public DynamicVersionController(DynamicVersionService dynamicVersionService) {
        this.dynamicVersionService = dynamicVersionService;
    }

    /**
     * 添加版本信息
     *
     * @param map 模型（携带参数）
     * @return 添加版本号页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        DynamicVersion version = new DynamicVersion();
        int result = this.dynamicVersionService.add(version);
        map.addAttribute("result", result);
        return "addVersion";
    }

    /**
     * 获取最后一次发布的版本号
     *
     * @return 最新发布的版本号
     */
    @RequestMapping(value = "/lastVersion", method = RequestMethod.GET)
    @ResponseBody
    public Long getLastVersion() {
        Long versionId;
        RedisUtil redisUtil = RedisUtil.getInstance();
        String result = String.valueOf(redisUtil.get(RedisKey.GATEWAY_VERSION_KEY));
        if (!StringUtils.isEmpty(result)) {
            versionId = Long.valueOf(result);
        } else {
            versionId = dynamicVersionService.getLastVersion();
            redisUtil.set(RedisKey.GATEWAY_VERSION_KEY, versionId);
        }
        return versionId;
    }


    /**
     * 打开发布版本列表页面
     *
     * @param model 模型
     * @return 版本list页面
     */
    @GetMapping("listAll")
    public String listAll(Model model) {
        model.addAttribute("list", this.dynamicVersionMapper.selectList(null));
        return "versionlist";
    }
}
