package ru.asmsoft.search.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Search condition DTO
 */
@Data
@NoArgsConstructor
public class SearchCondition {
    private Logic logic;
    private FieldType type;
    private String field;
    private String operator;
    private String value;
    private List<String> values;

    public Condition<? extends Comparable<?>> into() {
        switch(type) {
            case BOOLEAN:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Boolean.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Boolean::valueOf).collect(Collectors.toList())
                );
            case BYTE:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Byte.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Byte::valueOf).collect(Collectors.toList())
                );
            case SHORT:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Short.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Short::valueOf).collect(Collectors.toList())
                );
            case INTEGER:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Integer.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Integer::valueOf).collect(Collectors.toList())
                );
            case LONG:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Long.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Long::valueOf).collect(Collectors.toList())
                );
            case FLOAT:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Float.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Float::valueOf).collect(Collectors.toList())
                );
            case DOUBLE:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : Double.valueOf(value),
                    values == null ? Collections.emptyList() : values.stream().map(Double::valueOf).collect(Collectors.toList())
                );
            case BIGINTEGER:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : new BigInteger(value),
                    values == null ? Collections.emptyList() : values.stream().map(BigInteger::new).collect(Collectors.toList())
                );
            case BIGDECIMAL:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : new BigDecimal(value),
                    values == null ? Collections.emptyList() : values.stream().map(BigDecimal::new).collect(Collectors.toList())
                );
            case DATE:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : LocalDate.parse(value),
                    values == null ? Collections.emptyList() : values.stream().map(LocalDate::parse).collect(Collectors.toList())
                );
            case DATETIME:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : LocalDateTime.parse(value),
                    values == null ? Collections.emptyList() : values.stream().map(LocalDateTime::parse).collect(Collectors.toList())
                );
            case CHARACTER:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : value.charAt(0),
                    values.stream().map(v -> v.charAt(0)).collect(Collectors.toList())
                );
            default:
                return new Condition<>(
                    field, operator, logic,
                    value == null ? null : value,
                    values == null ? Collections.emptyList() : values
                );
        }
    }
}
