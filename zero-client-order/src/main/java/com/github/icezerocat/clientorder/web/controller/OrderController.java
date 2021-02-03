package com.github.icezerocat.clientorder.web.controller;

import com.github.icezerocat.clientorder.entity.Order;
import com.github.icezerocat.clientorder.mapper.OrderMapper;
import com.github.icezerocat.zeroopenfeign.client.service.ClientAccountFeignService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 订单控制器
 * CreateDate:  2021/1/28 21:25
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderMapper orderMapper;
    private final ClientAccountFeignService clientAccountFeignService;

    /**
     * 获取所有订单
     *
     * @return list
     */
    @GetMapping("getAll")
    public List<Order> getAll() {
        return this.orderMapper.selectList(null);
    }

    /**
     * 插入
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @GetMapping("insert")
    public void insert() {
        Order order = new Order();
        order.setUserId(0L);
        order.setProductId(0L);
        order.setCount(0L);
        order.setMoney(new BigDecimal("0"));
        order.setStatus(0L);
        this.orderMapper.insert(order);
        this.clientAccountFeignService.insert();
    }
}
