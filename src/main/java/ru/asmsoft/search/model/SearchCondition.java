package ru.asmsoft.search.model;

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
    private Expression expression;
    private FieldType type;
    private String field;
    private String operator;
    private Object value;

    public Condition<? extends Comparable<?>> into(Class<?> fieldType) {
        String stringValue = null;
        List<String> values = null;
        if (value instanceof List) {
            values = ((List<Object>) (value)).stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        } else {
            stringValue = String.valueOf(value);
        }

        if (fieldType.isAssignableFrom(Boolean.class) || fieldType.isAssignableFrom(boolean.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Boolean::valueOf);
        } else if (fieldType.isAssignableFrom(Byte.class) || fieldType.isAssignableFrom(byte.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Byte::valueOf);
        } else if (fieldType.isAssignableFrom(Short.class) || fieldType.isAssignableFrom(short.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Short::valueOf);
        } else if (fieldType.isAssignableFrom(Integer.class) || fieldType.isAssignableFrom(int.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Integer::valueOf);
        } else if (fieldType.isAssignableFrom(Long.class) || fieldType.isAssignableFrom(long.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Long::valueOf);
        } else if (fieldType.isAssignableFrom(Float.class) || fieldType.isAssignableFrom(float.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Float::valueOf);
        }else if (fieldType.isAssignableFrom(Double.class) || fieldType.isAssignableFrom(double.class)) {
            return Condition.of(field, operator, expression, stringValue, values, Double::valueOf);
        } else if (fieldType.isAssignableFrom(BigInteger.class)) {
            return Condition.of(field, operator, expression, stringValue, values, BigInteger::new);
        } else if (fieldType.isAssignableFrom(BigDecimal.class)) {
            return Condition.of(field, operator, expression, stringValue, values, BigDecimal::new);
        } else if (fieldType.isAssignableFrom(LocalDate.class) || fieldType.isAssignableFrom(Date.class)) {
            return Condition.of(field, operator, expression, stringValue, values, LocalDate::parse);
        } else if (fieldType.isAssignableFrom(LocalDateTime.class) || fieldType.isAssignableFrom(Timestamp.class)) {
            return Condition.of(field, operator, expression, stringValue, values, LocalDateTime::parse);
        } else if (fieldType.isAssignableFrom(Character.class) || fieldType.isAssignableFrom(char.class)) {
            return Condition.of(field, operator, expression, stringValue, values, r -> r.charAt(0));
        } else {
            return Condition.of(field, operator, expression, stringValue, values, r -> r);
        }
    }
}
