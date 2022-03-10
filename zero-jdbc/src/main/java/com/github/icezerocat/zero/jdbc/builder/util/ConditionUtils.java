package com.github.icezerocat.zero.jdbc.builder.util;


import com.github.icezerocat.zero.jdbc.builder.SQLBuilder;
import com.github.icezerocat.zero.jdbc.builder.Tuple2;
import com.github.icezerocat.zero.jdbc.builder.entry.Column;
import com.github.icezerocat.zero.jdbc.builder.entry.Constants;
import com.github.icezerocat.zero.jdbc.builder.enums.Operator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dragons
 * @date 2021/11/10 10:45
 */
public class ConditionUtils {

    private final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static String parseConditionColumn(String column1, Operator option, String column2) {
        return column1 + " " + option.getOptions().get(0) + " " + column2;
    }

    public static Tuple2<String, Object[]> parsePrecompileCondition(String column, Operator option, Object ...value) {
        if (option == Operator.IN || option == Operator.NOT_IN) {
            StringBuilder precompileTemplateBuilder = new StringBuilder(column + " " + option.getOptions().get(0));
            List<Object> vs = (List<Object>) Arrays.stream(value).flatMap(e -> {
                if (e instanceof Collection) return ((Collection) e).stream();
                return Stream.of(e);
            }).collect(Collectors.toList());
            for (int i = 0; i < vs.size(); i++) {
                if (i > 0) {
                    precompileTemplateBuilder.append(", ");
                }
                precompileTemplateBuilder.append("?");
            }
            precompileTemplateBuilder.append(option.getOptions().get(1));
            return Tuple2.of(precompileTemplateBuilder.toString(), vs.toArray());
        } else if (option == Operator.BETWEEN_AND) {
            String precompileTemplate = column + " " + option.getOptions().get(0) + " ? " + option.getOptions().get(1) + " ?";
            List<Object> vs = (List<Object>) Arrays.stream(value).flatMap(e -> {
                if (e instanceof Collection) return ((Collection) e).stream();
                return Stream.of(e);
            }).collect(Collectors.toList());
            return Tuple2.of(precompileTemplate, new Object[]{vs.get(0), vs.get(1)});
        } else if (option == Operator.LLIKE || option == Operator.NOT_LLIKE) {
            return Tuple2.of(column + " " + option.getOptions().get(0) + " ?", new Object[]{"%" + value[0]});
        } else if (option == Operator.RLIKE || option == Operator.NOT_RLIKE) {
            return Tuple2.of(column + " " + option.getOptions().get(0) + " ?", new Object[]{value[0] + "%"});
        } else if (option == Operator.LRLIKE || option == Operator.NOT_LRLIKE) {
            return Tuple2.of(column + " " + option.getOptions().get(0) + " ?", new Object[]{"%" + value[0] + "%"});
        } else if (option == Operator.IS_NULL || option == Operator.NOT_NULL) {
            return Tuple2.of(column + " " + option.getOptions().get(0), Constants.EMPTY_OBJECT_ARRAY);
        }
        return Tuple2.of(column + " " + option.getOptions().get(0) + " ?", new Object[]{value[0]});
    }

    public static String parseCondition(String column, Operator option, Object ...value) {
        if (option == Operator.IN || option == Operator.NOT_IN) {
            return column + " " + option.getOptions().get(0) + Arrays.stream(value)
                .flatMap(e -> {
                    if (e instanceof Collection) return ((Collection) e).stream();
                    return Stream.of(e);
                })
                .map(ConditionUtils::parseValue).collect(Collectors.joining(", ")) + option.getOptions().get(1);
        } else if (option == Operator.BETWEEN_AND) {
            return column + " " + option.getOptions().get(0) + " " +
                Arrays.stream(value).flatMap(e -> {
                    if (e instanceof Collection) return ((Collection) e).stream();
                    return Stream.of(e);
                }).limit(2).map(ConditionUtils::parseValue).collect(Collectors.joining(" " + option.getOptions().get(1) + " "));
        } else if (option == Operator.LLIKE || option == Operator.NOT_LLIKE) {
            return column + " " + option.getOptions().get(0) + " " +  parseValue("%" + value[0]);
        } else if (option == Operator.RLIKE || option == Operator.NOT_RLIKE) {
            return column + " " + option.getOptions().get(0) + " " +  parseValue(value[0] + "%");
        } else if (option == Operator.LRLIKE || option == Operator.NOT_LRLIKE) {
            return column + " " + option.getOptions().get(0) + " " +  parseValue("%"+ value[0] + "%");
        } else if (option == Operator.IS_NULL || option == Operator.NOT_NULL) {
            return column + " " + option.getOptions().get(0);
        }
        return column + " " + option.getOptions().get(0) + " " + parseValue(value[0]);
    }

    public static String parseValue(Object value) {
        if (value instanceof CharSequence) return "'" + value.toString().replace("'", "\\'") + "'";
        else if (value instanceof Column) return ((Column) value).getName();
        else if (value instanceof SQLBuilder) return "(" + ((SQLBuilder) value).build() + ")";
        else if (value instanceof Date) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);
            return parseValue(formatter.format(value));
        } else if (value instanceof TemporalAccessor) {
            return parseValue(DATE_TIME_FORMATTER.format((TemporalAccessor) value));
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        else if (value instanceof Collection) {
            // multi support
            return((Collection<?>) value).stream().map(ConditionUtils::parseValue).collect(Collectors.joining(", "));
        }
        return String.valueOf(value);
    }

    public static String parseTemplate(String template, Object ...values) {
        if (values.length == 0) return template;
        // support Collection
        values = Arrays.stream(values).flatMap(e -> {
            if (e instanceof Collection) return ((Collection) e).stream();
            return Stream.of(e);
        }).toArray();
        for (Object value : values) {
            int index = template.indexOf("?#");
            if (index >= 0) template = parseTemplateColumn(template, index, value);
            else if ((index = template.indexOf("?")) >= 0) template = parseTemplateValue(template, index, value);
            else return template;
        }
        return template;
    }

    private static String parseTemplateValue(String template, int index, Object value) {
        if (index == 0) template = parseValue(value) + template.substring(1);
        else if (index == template.length() - 1) template = template.substring(0, template.length() - 1) + parseValue(value);
        else template = template.substring(0, index) + parseValue(value) + template.substring(index + 1);
        return template;
    }

    private static String parseTemplateColumn(String template, int index, Object value) {
        if (index == 0) template = value + template.substring(2);
        else if (index == template.length() - 2) template = template.substring(0, template.length() - 2) + value;
        else template = template.substring(0, index) + value + template.substring(index + 2);
        return template;
    }
}
