package com.github.icezerocat.zero.jdbc.builder.inner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author dragons
 * @date 2021/12/7 14:16
 */
public class ClassMetadata {

    private final String className;

    private final Map<String, Field> fields;

    private final Map<String, Method> setterMethods;

    private final Map<String, Method> getterMethods;

    private final Map<String, Method> normalMethods;

    public ClassMetadata(Class<?> clazz) {
        this(clazz, Collections.emptyList(), Collections.emptyList());
    }

    public ClassMetadata(Class<?> clazz, List<String> ignoreFields, List<String> ignoreMethods) {
        className = clazz.getName();
        fields = new HashMap<>();
        setterMethods = new HashMap<>();
        getterMethods = new HashMap<>();
        normalMethods = new HashMap<>();
        Class<?> c = clazz;
        while (c != null) {
            Field[] declaredFields = c.getDeclaredFields();
            Method[] declaredMethods = c.getDeclaredMethods();
            Arrays.stream(declaredFields).filter(e -> !ignoreFields.contains(e.getName())).forEach(e -> fields.putIfAbsent(e.getName(), e));
            for (Method declaredMethod : declaredMethods) {
                if (!declaredMethod.isBridge() && !ignoreMethods.contains(declaredMethod.getName())) {
                    if (declaredMethod.getName().startsWith("set")) {
                        setterMethods.put(declaredMethod.getName(), declaredMethod);
                    }
                    else if (declaredMethod.getName().startsWith("get")) {
                        getterMethods.put(declaredMethod.getName(), declaredMethod);
                    }
                    else {
                        normalMethods.put(declaredMethod.getName(), declaredMethod);
                    }
                }
            }
            c = c.getSuperclass();
        }
    }

    public String getClassName() {
        return className;
    }

    public List<Field> getFields() {
        return new ArrayList<>(fields.values());
    }

    public List<Method> getSetterMethods() {
        return new ArrayList<>(setterMethods.values());
    }

    public List<Method> getGetterMethods() {
        return new ArrayList<>(getterMethods.values());
    }

    public List<Method> getNormalMethods() {
        return new ArrayList<>(normalMethods.values());
    }

    public Field getField(String fieldName) {
        return fields.get(fieldName);
    }

    public Method getMethod(String methodName) {
        Method method;
        if ((method = setterMethods.get(methodName)) != null) return method;
        if ((method = getterMethods.get(methodName)) != null) return method;
        if ((method = normalMethods.get(methodName)) != null) return method;
        return null;
    }

    public Method getSetterMethod(String methodName) {
        return setterMethods.get(methodName);
    }

    public Method getGetterMethod(String methodName) {
        return getterMethods.get(methodName);
    }

    public Method getNormalMethod(String methodName) {
        return normalMethods.get(methodName);
    }
}
