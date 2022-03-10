package com.github.icezerocat.zero.validation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 请求解析配置
 * CreateDate:  2022/2/21 19:32
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@Configuration
public class ResolverConfig {
    @Resource
    private RequestMappingHandlerAdapter adapter;

    @PostConstruct
    public void injectSelfMethodArgumentResolver() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
        List<HandlerMethodArgumentResolver> argumentResolvers = adapter.getArgumentResolvers();
        RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor = null;
        ModelAttributeMethodProcessor modelAttributeMethodProcessor = null;
        if (argumentResolvers != null) {
            log.debug("扩展请求参数解析器");
            resolvers.add(new ExtendRequestParamArgumentResolver(false));
            for (HandlerMethodArgumentResolver resolver : argumentResolvers) {
                if (resolver instanceof RequestResponseBodyMethodProcessor) {
                    requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) resolver;
                } else if (resolver instanceof ModelAttributeMethodProcessor) {
                    modelAttributeMethodProcessor = (ModelAttributeMethodProcessor) resolver;
                } else {
                    resolvers.add(resolver);
                }
            }
            // 合并表单提交处理和@RequestBody
            resolvers.add(new JsonAndFormArgumentResolver(modelAttributeMethodProcessor, requestResponseBodyMethodProcessor));
            adapter.setArgumentResolvers(resolvers);
        }
    }
}
