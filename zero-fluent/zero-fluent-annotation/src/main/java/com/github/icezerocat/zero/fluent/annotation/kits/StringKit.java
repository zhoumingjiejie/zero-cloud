package com.github.icezerocat.zero.fluent.annotation.kits;

/**
 * StringKit: 字符串处理工具
 *
 * @author wudarui
 */
public class StringKit {
    /**
     * 将Exception转换为RuntimeException
     *
     * @param e Exception
     * @return RuntimeException
     */
    public static RuntimeException wrap(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean notBlank(String text) {
        return text != null && !text.trim().isEmpty();
    }


    /**
     * 实体首字母小写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String lowerFirst(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    /**
     * 实体首字母小写
     *
     * @param name 待转换的字符串
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static final String PRE_SET = "set";

    public static final String PRE_IS = "is";

    public static final String PRE_GET = "get";

    public static final String PRE_FIND = "find";
}
