package com.github.icezerocat.zero.jdbc.builder.inner;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilderException;
import com.github.icezerocat.zero.jdbc.builder.annotation.Column;
import com.github.icezerocat.zero.jdbc.builder.annotation.Primary;
import com.github.icezerocat.zero.jdbc.builder.annotation.Table;
import com.github.icezerocat.zero.jdbc.builder.entry.Alias;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内部专用-对象映射工具类
 *
 * @author zero
 * @date 2021/12/7 14:09
 */
public class ObjectMapperUtils {

    private ObjectMapperUtils() {
    }

    private static String sep = "_";

    private final static Map<Class<?>, ClassMetadata> CLASS_METADATA_MAP = new ConcurrentHashMap<>();

    private final static Map<Class<?>, List<Alias>> PRIMARY_MAP = new ConcurrentHashMap<>();

    private final static Map<String, Constructor> CONSTRUCTOR_MAP = new ConcurrentHashMap<>();

    private final static Map<String, Object> SINGLE_OBJECT_MAP = new ConcurrentHashMap<>();

    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null) {
            return humpNameToUnderlinedName(clazz.getSimpleName(), sep);
        }
        return table.value();
    }

    public static List<String> getColumns(Class<?> clazz) {
        return getColumnFields(clazz).stream().map(Alias::getOrigin).collect(Collectors.toList());
    }

    public static <T extends Annotation> T getFieldAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
        Field field = getField(clazz, fieldName);
        T annotation = null;
        if (field != null) {
            annotation = field.getAnnotation(annotationClass);
        }
        if (annotation == null) {
            ClassMetadata meta = getClassMeta(clazz, Collections.emptyList(), Collections.emptyList());
            Method getterMethod = meta.getGetterMethod("get" + title(fieldName));
            if (getterMethod != null) {
                annotation = getterMethod.getAnnotation(annotationClass);
            }
            if (annotation == null) {
                Method setterMethod = meta.getSetterMethod("set" + title(fieldName));
                if (setterMethod != null) {
                    annotation = setterMethod.getAnnotation(annotationClass);
                }
            }
        }
        return annotation;
    }

    /**
     * 获取列字段
     *
     * @param clazz 类
     * @return 获取类字段
     */
    public static List<Alias> getColumnFields(Class<?> clazz) {
        ClassMetadata meta = getClassMeta(clazz, Collections.emptyList(), Collections.emptyList());
        if (meta == null) {
            return Collections.emptyList();
        }
        List<Alias> columns = new ArrayList<>();
        meta.getFields().stream().filter(e -> !Modifier.isFinal(e.getModifiers())).forEach(e -> {
            String columnName = humpNameToUnderlinedName(e.getName(), sep), aliasName = e.getName();
            Method getterMethod = meta.getGetterMethod("get" + title(e.getName()));
            Method setterMethod = meta.getSetterMethod("set" + title(e.getName()));

            Column columnAnnotation = e.getAnnotation(Column.class);
            if (columnAnnotation == null) {
                if (getterMethod != null) {
                    columnAnnotation = getterMethod.getAnnotation(Column.class);
                }
                if (columnAnnotation == null && setterMethod != null) {
                    columnAnnotation = setterMethod.getAnnotation(Column.class);
                }
            }
            if (columnAnnotation != null && !"".equals(columnAnnotation.value())) {
                columnName = columnAnnotation.value();
            }
            columns.add(Alias.of(columnName, aliasName));
        });
        return columns;
    }

    public static Object getAttr(Object obj, String[] attr, int index) throws IllegalAccessException, InvocationTargetException {
        Object result;
        if (obj instanceof Collection) {
            Collection o = (Collection) obj;
            if (o.isEmpty()) {
                return null;
            }
            result = o.stream().map(e -> {
                try {
                    return getAttr(e, attr, index);
                } catch (IllegalAccessException | InvocationTargetException illegalAccessException) {
                    throw new SQLBuilderException("Attribute acquisition failure, type: \"" + e.getClass().getName() + "\", attr: " + attr, illegalAccessException);
                }
            }).collect(Collectors.toList());
        } else if (obj instanceof Map) {
            result = ((Map<?, ?>) obj).get(attr);
        } else {
            result = getFieldValue(obj, attr[index]);
        }
        if (index == attr.length - 1) {
            return result;
        }
        return getAttr(result, attr, index + 1);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        ClassMetadata classMeta = getClassMeta(clazz, Collections.emptyList(), Collections.emptyList());
        if (classMeta == null) {
            return null;
        }
        return classMeta.getField(fieldName);
    }

    public static <T, V> V getFieldValue(T obj, String fieldName) throws IllegalAccessException, InvocationTargetException {
        ClassMetadata classMeta = getClassMeta(obj.getClass(), Collections.emptyList(), Collections.emptyList());
        if (classMeta == null) {
            return null;
        }

        Field field = classMeta.getField(fieldName);

        if (field == null) {
            Method method = classMeta.getGetterMethod("get" + title(fieldName));
            if (method != null) {
                return (V) method.invoke(obj);
            } else {
                return null;
            }
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return (V) field.get(obj);
    }

    public static <T> T getInstance(Class<T> clazz, Class<?>[] parameterTypes, Object... params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> constructor = getConstructor(clazz, parameterTypes);
        return constructor.newInstance(params);
    }

    public static Object getSingleObject(Class<?> clazz, Object... args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String key = clazz.getName() + "@" + Arrays.stream(args).map(e -> e.getClass().getName()).collect(Collectors.joining("#"));
        Object obj = SINGLE_OBJECT_MAP.get(key);
        if (obj == null) {
            synchronized (SINGLE_OBJECT_MAP) {
                obj = SINGLE_OBJECT_MAP.get(key);
                if (obj == null) {
                    if (args.length == 0) {
                        obj = clazz.getConstructor().newInstance();
                    } else {
                        obj = clazz.getConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class[]::new));
                    }
                    SINGLE_OBJECT_MAP.put(key, obj);
                }
            }
        }
        return obj;
    }

    public static List<Alias> getPrimaries(Class<?> clazz) {
        List<Alias> primaries = PRIMARY_MAP.get(clazz);
        if (primaries == null) {
            synchronized (PRIMARY_MAP) {
                primaries = PRIMARY_MAP.get(clazz);
                if (primaries == null) {
                    ClassMetadata classMeta = getClassMeta(clazz, Collections.emptyList(), Collections.emptyList());
                    if (classMeta == null) {
                        return null;
                    }
                    List<Field> fields = classMeta.getFields();
                    List<Method> getterMethods = classMeta.getGetterMethods();
                    List<Method> setterMethods = classMeta.getSetterMethods();

                    Set<Alias> primariesSet = fields.stream().filter(e -> e.getAnnotation(Primary.class) != null).map(e -> {
                        Column column = e.getAnnotation(Column.class);
                        if (column == null) {
                            return Alias.of(humpNameToUnderlinedName(e.getName(), sep), e.getName());
                        }
                        return Alias.of(column.value(), e.getName());
                    }).collect(Collectors.toSet());

                    primariesSet.addAll(fieldMethodToPrimarySet(getterMethods));

                    primariesSet.addAll(fieldMethodToPrimarySet(setterMethods));

                    primaries = new ArrayList<>(primariesSet);
                    PRIMARY_MAP.put(clazz, primaries);
                }
            }
        }
        return primaries;
    }

    public static <T> Map<String, Object> getColumnAndValues(T obj, List<Alias> fields) throws IllegalAccessException, InvocationTargetException {
        if (fields == null || fields.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Object> fieldMapping = new HashMap<>();
        for (Alias field : fields) {
            Object value = getFieldValue(obj, field.getAlias());
            if (value != null) {
                fieldMapping.put(field.getOrigin(), value);
            }
        }
        return fieldMapping;
    }

    protected static Set<Alias> fieldMethodToPrimarySet(List<Method> methods) {
        if (methods == null || methods.isEmpty()) {
            return Collections.emptySet();
        }
        return methods.stream().filter(e -> e.getAnnotation(Primary.class) != null).map(e -> {
            Column column = e.getAnnotation(Column.class);
            String fieldName = e.getName().substring(3);
            if (fieldName.length() > 1) {
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
            } else {
                fieldName = fieldName.toLowerCase();
            }
            if (column == null) {
                return Alias.of(humpNameToUnderlinedName(e.getName().substring(3), sep), fieldName);
            }
            return Alias.of(column.value(), fieldName);
        }).collect(Collectors.toSet());
    }

    protected static String humpNameToUnderlinedName(String name, String sep) {
        StringBuilder nameBuilder = new StringBuilder();
        for (char c : name.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                if (nameBuilder.length() > 0) {
                    nameBuilder.append(sep);
                }
                nameBuilder.append((char) (c + 32));
            } else {
                nameBuilder.append(c);
            }
        }
        return nameBuilder.toString();
    }

    protected static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>[] parameterTypes) throws NoSuchMethodException {
        String key = clazz.getName() + "@" + Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining("#"));
        Constructor<T> constructor = CONSTRUCTOR_MAP.get(key);
        if (constructor == null) {
            synchronized (CONSTRUCTOR_MAP) {
                constructor = CONSTRUCTOR_MAP.get(key);
                if (constructor == null) {
                    constructor = clazz.getDeclaredConstructor(parameterTypes);
                    if (!constructor.isAccessible()) {
                        constructor.setAccessible(true);
                    }
                    CONSTRUCTOR_MAP.put(key, constructor);
                }
            }
        }
        return constructor;
    }

    protected static String title(String fieldName) {
        if (fieldName == null || "".equals(fieldName)) {
            return "";
        }
        if (fieldName.length() == 1) {
            return fieldName.toUpperCase();
        }
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    protected static ClassMetadata getClassMeta(Class<?> clazz, List<String> ignoreFields, List<String> ignoreMethods) {
        ClassMetadata classMetadata = CLASS_METADATA_MAP.get(clazz);
        if (classMetadata == null) {
            synchronized (CLASS_METADATA_MAP) {
                classMetadata = CLASS_METADATA_MAP.get(clazz);
                if (classMetadata == null) {
                    classMetadata = new ClassMetadata(clazz, ignoreFields, ignoreMethods);
                    CLASS_METADATA_MAP.put(clazz, classMetadata);
                }
            }
        }
        return classMetadata;
    }
}
