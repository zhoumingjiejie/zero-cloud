package com.github.icezerocat.zero.validation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: Json 和表单参数解析器:数据的 Resolver 分发
 * CreateDate:  2022/2/21 18:58
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
public class JsonAndFormArgumentResolver implements HandlerMethodArgumentResolver {

    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;
    private ModelAttributeMethodProcessor modelAttributeMethodProcessor;
    private ExtendRequestParamArgumentResolver extendRequestParamArgumentResolver = new ExtendRequestParamArgumentResolver(false);

    public JsonAndFormArgumentResolver() {
    }

    public JsonAndFormArgumentResolver(ModelAttributeMethodProcessor methodProcessor, RequestResponseBodyMethodProcessor bodyMethodProcessor) {
        this.modelAttributeMethodProcessor = methodProcessor;
        this.requestResponseBodyMethodProcessor = bodyMethodProcessor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.debug("支持参数:{}", parameter);
        return modelAttributeMethodProcessor.supportsParameter(parameter)
                || requestResponseBodyMethodProcessor.supportsParameter(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.debug("解决参数:{}", parameter);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            if (HttpMethod.GET.matches(request.getMethod().toUpperCase())) {
                return modelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
            }

            if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                return requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
            }
        }
        return modelAttributeMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}
