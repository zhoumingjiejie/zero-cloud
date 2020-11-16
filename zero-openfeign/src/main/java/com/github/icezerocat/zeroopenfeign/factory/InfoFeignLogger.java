package com.github.icezerocat.zeroopenfeign.factory;

import org.slf4j.Logger;

/**
 * Description: info feign 日志
 * CreateDate:  2020/11/16 9:39
 *
 * @author zero
 * @version 1.0
 */
public class InfoFeignLogger extends feign.Logger {

    private final Logger logger;

    public InfoFeignLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
