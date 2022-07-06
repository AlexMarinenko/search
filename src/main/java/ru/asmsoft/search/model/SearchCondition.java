package ru.asmsoft.search.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search condition DTO
 */
@Data
@NoArgsConstructor
public class SearchCondition {

    @Schema(description = "Expression to join the condition. AND and OR values available")
    private Expression expression;

    @Schema(description = "The field name to apply the condition to")
    private String field;
    @Schema(description = "The operator for condition. Available operators: \"=\", \"!=\", \">=\", \"<=\", \"LIKE\", \"IN\"")
    private String operator;
    @Schema(description = "The second operand for condition. Array for \"IN\"")
    private Object value;

    @Schema(description = "The second operand for condition. Array for \"IN\"")
    private List<Object> values;

    public Condition<? extends Comparable<?>> into(Class<?> fieldType) {
        String stringValue = null;
        List<String> valuesList = null;
        if (value instanceof List) {
            valuesList = ((List<Object>) (value)).stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        } else if (values != null) {
            valuesList = values.stream().map(String::valueOf).collect(Collectors.toList());
        } else {
            stringValue = String.valueOf(value);
        }

        if (fieldType.isAssignableFrom(Boolean.class) || fieldType.isAssignableFrom(boolean.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Boolean::valueOf);
        } else if (fieldType.isAssignableFrom(Byte.class) || fieldType.isAssignableFrom(byte.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Byte::valueOf);
        } else if (fieldType.isAssignableFrom(Short.class) || fieldType.isAssignableFrom(short.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Short::valueOf);
        } else if (fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(int.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Integer::valueOf);
        } else if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(long.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Long::valueOf);
        } else if (fieldType.isAssignableFrom(Float.class) || fieldType.isAssignableFrom(float.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Float::valueOf);
        }else if (fieldType.isAssignableFrom(Double.class) || fieldType.isAssignableFrom(double.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, Double::valueOf);
        } else if (fieldType.isAssignableFrom(BigInteger.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, BigInteger::new);
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, BigDecimal::new);
        } else if (fieldType.isAssignableFrom(LocalDate.class) || fieldType.isAssignableFrom(Date.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, LocalDate::parse);
        } else if (fieldType.isAssignableFrom(LocalDateTime.class) || fieldType.isAssignableFrom(Timestamp.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, LocalDateTime::parse);
        } else if (fieldType.isAssignableFrom(Character.class) || fieldType.isAssignableFrom(char.class)) {
            return Condition.of(field, operator, expression, stringValue, valuesList, r -> r.charAt(0));
        } else {
            return Condition.of(field, operator, expression, stringValue, valuesList, r -> r);
        }
    }
}
