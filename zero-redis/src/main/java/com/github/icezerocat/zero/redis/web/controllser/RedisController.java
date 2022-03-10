package com.github.icezerocat.zero.redis.web.controllser;

import com.github.icezerocat.zero.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: redis控制器
 * CreateDate:  2021/12/24 14:22
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@RequestMapping
@RestController
public class RedisController {

    @GetMapping("say")
    public void say() {
        RedisUtil.getInstance().set("zero", "hello world");
    }

    @GetMapping("sayDel")
    public void sayDel() {
        RedisUtil.getInstance().del("zero");
    }

    @GetMapping("addKey")
    public String addKey() {
        String timeMillis = String.valueOf(System.currentTimeMillis());
        log.debug(timeMillis);
        RedisUtil.getInstance().set(timeMillis, timeMillis, 3);
        return timeMillis;
    }
}
