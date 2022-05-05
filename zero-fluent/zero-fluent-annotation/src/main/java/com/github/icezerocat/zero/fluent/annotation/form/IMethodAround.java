package com.github.icezerocat.zero.fluent.annotation.form;

import com.github.icezerocat.zero.fluent.annotation.kits.KeyMap;
import com.github.icezerocat.zero.fluent.annotation.kits.SegmentLocks;
import com.github.icezerocat.zero.fluent.annotation.form.meta.MethodMeta;

import java.lang.reflect.Method;

/**
 * 对form service接口进行切面处理
 *
 * @author wudarui
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IMethodAround {

    KeyMap<MethodMeta> METHOD_METAS_CACHED = new KeyMap<>();
    /**
     * 按 Method.toString() 签名进行加锁
     */
    SegmentLocks<String> MethodLock = new SegmentLocks<>(16);

    /**
     * 根据方法定义构造方法元数据(从缓存获取)
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @return 方法元数据
     */
    default MethodMeta cache(Class entityClass, Method method) {
        String mName = method.toString();
        MethodLock.lockDoing(METHOD_METAS_CACHED::containsKey, mName, () -> METHOD_METAS_CACHED.put(mName, this.before(entityClass, method)));
        return METHOD_METAS_CACHED.get(mName);
    }

    /**
     * 根据方法定义构造方法元数据
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @return 方法元数据
     */
    MethodMeta before(Class entityClass, Method method);

    /**
     * 对入参进行预处理
     *
     * @param meta 方法元数据
     * @param args 原始入参
     * @return 处理后的入参
     */
    default Object[] before(MethodMeta meta, Object... args) {
        return args;
    }

    /**
     * 结果值处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param args        原始入参
     * @param result      FormService执行结果
     * @return 原始方法的返回值
     */
    Object after(Class entityClass, Method method, Object[] args, Object result);

    /**
     * 异常值处理
     *
     * @param entityClass 执行的表Entity类
     * @param method      执行方法
     * @param args        原始入参
     * @param exception   FormService执行过程中抛出的异常
     * @return 原始方法的返回值
     */
    default Object after(Class entityClass, Method method, Object[] args, RuntimeException exception) {
        throw exception;
    }
}
