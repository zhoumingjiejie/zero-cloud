package com.github.icezerocat.config.zeroconfiggateway.web.controller;

import com.alibaba.fastjson.JSON;
import com.github.icezerocat.config.zeroconfiggateway.entity.GatewayRoutes;
import com.github.icezerocat.config.zeroconfiggateway.mapper.GatewayRoutesMapper;
import com.github.icezerocat.config.zeroconfiggateway.service.RoutesService;
import com.github.icezerocat.zerocore.constants.RedisKey;
import com.github.icezerocat.zerocore.utils.RedisUtil;
import com.github.icezerocat.zeroopenfeign.gateway.model.GatewayRouteDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 网关路由数据控制器
 * CreateDate:  2020/11/14 1:09
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Controller
@RequestMapping("gateway-routes")
public class GatewayRoutesController {

    @Resource
    private GatewayRoutesMapper gatewayRoutesMapper;
    private final RoutesService routesService;

    public GatewayRoutesController(RoutesService routesService) {
        this.routesService = routesService;
    }

    /**
     * 获取所有动态路由信息
     *
     * @return 动态路由数据
     */
    @RequestMapping("/routes")
    @ResponseBody
    public String getRouteDefinitions() {
        //先从redis中取，再从mysql中取
        RedisUtil redisUtil = RedisUtil.getInstance();
        String result = JSON.toJSONString(redisUtil.lGet(RedisKey.GATEWAY_ROUTE_KEY, 0, -1));
        if (StringUtils.isEmpty(result)) {
            List<GatewayRouteDefinition> routeDefinitions = this.routesService.getRouteDefinitions();
            result = JSON.toJSONString(routeDefinitions);
            //再set到redis
            redisUtil.lSetAll(RedisKey.GATEWAY_ROUTE_KEY, routeDefinitions);
        }
        log.debug("路由信息：{}", result);
        return result;
    }


    /**
     * 打开添加路由页面
     *
     * @param map 模型（携带参数）
     * @return 添加路由页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        map.addAttribute("route", new GatewayRoutes());
        return "addRoute";
    }

    /**
     * 添加路由信息
     *
     * @param route 路由对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestBody GatewayRoutes route) {
        return this.gatewayRoutesMapper.insert(route) > 0 ? "success" : "fail";
    }

    /**
     * 编辑路由信息
     *
     * @param map 模型（携带返回数据）
     * @param id  路由id
     * @return 添加路由页面
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(ModelMap map, Long id) {
        map.addAttribute("route", this.gatewayRoutesMapper.selectById(id));
        return "addRoute";
    }

    /**
     * 添加路由信息
     *
     * @param route 路由数据
     * @return 更新路由结果
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public String edit(@RequestBody GatewayRoutes route) {
        return this.gatewayRoutesMapper.insert(route) > 0 ? "success" : "fail";
    }

    /**
     * 打开路由列表
     *
     * @param map 模型携带参数
     * @return 全部路由列表
     */
    @RequestMapping("/list")
    public String list(ModelMap map) {
        map.addAttribute("list", this.gatewayRoutesMapper.selectList(null));
        return "routelist";
    }

    /**
     * 删除路由
     *
     * @param id 路由ID
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public String delete(Long id) {
        GatewayRoutes gatewayRoutes = this.gatewayRoutesMapper.selectById(id);
        gatewayRoutes.setIsDel(1);
        this.gatewayRoutesMapper.insert(gatewayRoutes);
        return "delSuccess";
    }
}
