package com.github.icezerocat.zerotsf.listener;

import com.tencent.tsf.consul.config.watch.ConfigChangeCallback;
import com.tencent.tsf.consul.config.watch.ConfigChangeListener;
import com.tencent.tsf.consul.config.watch.ConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Description: 配置监听
 * CreateDate:  2022/6/13 11:43
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@ConfigChangeListener(prefix = "provider", async = true)
public class SimpleConfigurationListener implements ConfigChangeCallback {


    @Override
    public void callback(ConfigProperty lastConfigProperty, ConfigProperty currentConfigProperty) {
        log.debug("receive Consul Config Change Event >>>> ");
        log.debug(StringUtils.join("Last config: [", lastConfigProperty.getKey(), ":", lastConfigProperty.getValue()));
        log.debug(StringUtils.join("Current config: [", currentConfigProperty.getKey(), ":", currentConfigProperty.getValue()));
        System.out.println(lastConfigProperty);
        System.out.println(currentConfigProperty);
        System.out.println("receive Consul Config Change Event >>>> END");
    }
}
