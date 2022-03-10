package com.github.icezerocat.zero.validation.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: 扩展请求参数解析器
 * CreateDate:  2022/2/21 19:02
 *
 * @author zero
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("unused")
public class ExtendRequestParamArgumentResolver extends RequestParamMethodArgumentResolver {
    private static final String JSON_BODY_ATTRIBUTE = "JSON_REQUEST_BODY";

    public ExtendRequestParamArgumentResolver(boolean useDefaultResolution) {
        super(useDefaultResolution);
    }

    public ExtendRequestParamArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        super(beanFactory, useDefaultResolution);
    }

    @Override
    protected Object resolveName(@NotNull String name, @NotNull MethodParameter parameter, NativeWebRequest request) throws Exception {
        // 尝试 form 格式解析,url中的parame
        log.debug("解析名称, 尝试 form、json 格式解析：{} \n {}", name, parameter);
        Object arg = super.resolveName(name, parameter, request);
        // 解析失败时，尝试 JSON 格式解析
        if (arg == null) {
            JSONObject requestBody = this.getRequestBody(request);
            // JSON 解析失败，返回null
            if (requestBody == null) {
                return null;
            }
        }
        return arg;
    }

    /**
     * 基本类型解析
     *
     * @param parameterTypeName 参数类型名称
     * @param value             值
     * @return 对应类型值
     */
    private Object parsePrimitive(String parameterTypeName, Object value) {
        if (value == null) {
            return null;
        }

        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            return Boolean.valueOf(value.toString());
        }

        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            return Integer.valueOf(value.toString());
        }

        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }

        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            return Short.valueOf(value.toString());
        }

        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            return Long.valueOf(value.toString());
        }

        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            return Float.valueOf(value.toString());
        }

        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            return Double.valueOf(value.toString());
        }

        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            return Byte.valueOf(value.toString());
        }

        return null;
    }

    /**
     * 解析基本类型包装器
     *
     * @param parameterType 参数类型
     * @param value         值
     * @return 对应类型值
     */
    private Object parseBasicTypeWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            Number number = (Number) value;
            if (parameterType == Integer.class) {
                return number.intValue();
            } else if (parameterType == Short.class) {
                return number.shortValue();
            } else if (parameterType == Long.class) {
                return number.longValue();
            } else if (parameterType == Float.class) {
                return number.floatValue();
            } else if (parameterType == Double.class) {
                return number.doubleValue();
            } else if (parameterType == Byte.class) {
                return number.byteValue();
            }
        } else if (parameterType == Boolean.class || parameterType == String.class) {
            return value.toString();
        } else if (parameterType == Character.class) {
            return value.toString().charAt(0);
        }
        return null;
    }

    /**
     * 基本数据类型直接返回
     *
     * @param clazz 类
     * @return boolean判断
     */
    private boolean isBasicDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<>();
        classSet.add(String.class);
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * 获取body结构数据
     *
     * @param webRequest 原生网络请求
     * @return json
     */
    private JSONObject getRequestBody(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 尝试从 Request 中获取 JSONObject
        JSONObject jsonBody = (JSONObject) webRequest.getAttribute(JSON_BODY_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);
        if (jsonBody == null && request != null) {
            try {
                // 需要从输入流中获取参数，Json格式数据不能用request.getParameter(name)方式获得
                String jsonStr = IOUtils.toString(request.getInputStream());
                if (JsonValidator.validate(jsonStr)) {
                    jsonBody = JSON.parseObject(jsonStr);
                    webRequest.setAttribute(JSON_BODY_ATTRIBUTE, jsonBody, NativeWebRequest.SCOPE_REQUEST);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonBody;
    }
}
