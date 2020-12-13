package com.github.icezerocat.zerocore.exception;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.icezerocat.zerocommon.exception.ApiException;
import com.github.icezerocat.zerocommon.http.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;

/**
 * ProjectName: [icezero-system]
 * Package:     [com.githup.icezerocat.common.exception.ExceptionControllerAdvice]
 * Description: RestController全局异常
 * CreateDate:  2020/4/20 22:34
 *
 * @author 0.0.0
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ExceptionRestControllerAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 参数校验异常
     *
     * @param e 方法参数无效异常
     * @return 1001
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 然后提取错误提示信息进行返回
        return HttpResult.Build.getInstance().setCode(HttpStatus.CHECKPOINT.value()).setMessage("参数校验失败").setData(objectError.getDefaultMessage()).complete();
    }

    /**
     * 运行异常
     *
     * @param e 自定义异常
     * @return code
     */
    @ExceptionHandler(ApiException.class)
    public HttpResult apiExceptionHandler(ApiException e) {
        e.printStackTrace();
        return HttpResult.Build.getInstance().setCode(e.getCode()).setMessage("响应失败").setData(e.getMessage()).complete();
    }

    /**
     * 是否对返回类型做额外操作
     *
     * @param methodParameter 方法参数
     * @param aClass          Http消息转换器
     * @return boolean（默认：false。true才会执行beforeBodyWrite方法）
     */
    @Override
    public boolean supports(MethodParameter methodParameter, @NonNull Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是HttpResult那就没有必要进行额外的操作，返回false
        return !HttpResult.class.equals(methodParameter.getParameterType());
    }

    /**
     * 进行数据包装
     *
     * @param o                  对象
     * @param methodParameter    方法参数
     * @param mediaType          媒体类型
     * @param aClass             Http消息转换器
     * @param serverHttpRequest  服务器Http请求
     * @param serverHttpResponse 服务器Http响应
     * @return 封装数据
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class<? extends HttpMessageConverter<?>> aClass, @NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (String.class.equals(methodParameter.getParameterType())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在HttpResult里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(HttpResult.ok(o));
            } catch (JsonProcessingException e) {
                throw new ApiException("返回String类型错误");
            }
        }

        // 将原本的数据包装在HttpResult里
        return HttpResult.Build.getInstance().setCode(getCode(o)).setData(o).complete();
    }

    /**
     * 获取异常状态码
     *
     * @param o 对象
     * @return 状态码
     */
    private int getCode(Object o) {
        int code = 200;
        HttpResult httpResult = HttpResult.Build.getInstance().setData(o).complete();
        Class tClass = httpResult.getClass();

        JSONObject jsonObject = new JSONObject();
        Field[] declaredFields = tClass.getDeclaredFields();
        final String status = "status=";
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            try {
                jsonObject.put(fieldName, field.get(httpResult));
            } catch (IllegalAccessException ignored) {
            }
        }
        Object data = jsonObject.get("data");
        if (data != null) {
            String dataStr = String.valueOf(data);
            if (dataStr.contains(status)) {
                dataStr = dataStr.substring(dataStr.indexOf(status));
                dataStr = (dataStr.length() > status.length() && dataStr.contains(",") && dataStr.length() > dataStr.indexOf(",")) ?
                        dataStr.substring(status.length(), dataStr.indexOf(",")) : "";
                try {
                    code = Integer.parseInt(dataStr);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return code;
    }
}
