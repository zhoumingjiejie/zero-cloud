package com.github.icezerocat.zeroopenfeign.build;

import com.github.icezerocat.zeroopenfeign.factory.InfoFeignLogger;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.slf4j.LoggerFactory;

/**
 * Description: feignBuild构建类
 * CreateDate:  2020/12/9 0:04
 *
 * @author zero
 * @version 1.0
 */
public class FeignBuild {
    /**
     * 统一的 feign 接口实现类获取逻辑
     *
     * @param clazz feign接口class
     * @param url   请求base url
     * @return Feign接口对象
     */
    public static <T> T getFeignClient(Class<T> clazz, String url) {
        return Feign.builder()
                // 自定义日志类，继承 feign.Logger
                .logger(new InfoFeignLogger(LoggerFactory.getLogger(clazz)))
                // 日志级别
                .logLevel(Logger.Level.FULL)
                //5s超时，仅1次重试
                //.retryer(new Retryer.Default(5000, 5000, 1))
                .target(clazz, url);
    }

    /**
     * 统一的 feign 接口实现类获取逻辑
     *
     * @param clazz   feign接口class
     * @param url     请求base url
     * @param decoder 解码器
     * @param encoder 编码器
     * @return Feign接口对象
     */
    public static <T> T getFeignClient(Class<T> clazz, String url, Decoder decoder, Encoder encoder) {
        return Feign.builder()
                // 自定义日志类，继承 feign.Logger
                .logger(new InfoFeignLogger(LoggerFactory.getLogger(clazz)))
                // 日志级别
                .logLevel(Logger.Level.FULL)
                .decoder(decoder)
                .encoder(encoder)
                //5s超时，仅1次重试
                //.retryer(new Retryer.Default(5000, 5000, 1))
                .target(clazz, url);
    }
}
