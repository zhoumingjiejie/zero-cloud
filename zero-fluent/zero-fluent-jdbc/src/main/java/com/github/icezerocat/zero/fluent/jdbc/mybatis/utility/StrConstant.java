package com.github.icezerocat.zero.fluent.jdbc.mybatis.utility;

/**
 * fluent mybatis 常量定义
 *
 * @author darui.wu
 */
public interface StrConstant {
    /**
     * 空串
     */
    String EMPTY = "";

    /**
     * asc
     */
    String ASC = "ASC";
    /**
     * desc
     */
    String DESC = "DESC";
    /**
     * 格式化占位符
     */
    String STR_FORMAT = "%s";
    /**
     * 逗号
     */
    String COMMA = ",";
    /**
     * 分号
     */
    String SEMICOLON = ";";
    /**
     * 星号
     */
    String ASTERISK = "*";
    /**
     * 问号
     */
    String QUESTION_MARK = "?";
    /**
     * 空格
     */
    String SPACE = " ";

    String UNION = "UNION";

    String UNION_ALL = "UNION ALL";

    String COUNT_1 = "COUNT(1)";

    String COUNT_ASTERISK = "COUNT(*)";
    /**
     * 换行
     */
    String NEWLINE = "\n";
    /**
     * 逗号 + 空格
     */
    String COMMA_SPACE = COMMA + SPACE;

    String SEMICOLON_NEWLINE = SEMICOLON + NEWLINE;
    /**
     * 右花括号
     */
    String RIGHT_CURLY_BRACKET = "}";
    /**
     * "#{"
     */
    String HASH_MARK_LEFT_CURLY = "#{";
    /**
     * "${"
     */
    String DOLLAR_LEFT_CURLY = "${";
    /**
     * value字符常量
     */
    String STR_VALUE = "value";
    /**
     * 双引号
     */
    char DOUBLE_QUOTATION = '"';

    char DOT = '.';

    String DOT_STR = ".";
    /**
     * 时间格式化
     */
    String DateStrFormat = "yyyy-MM-dd HH:mm:ss";
}
