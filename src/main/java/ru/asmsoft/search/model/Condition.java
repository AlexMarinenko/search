package ru.asmsoft.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Search condition for specification
 * @param <T> type of Condition value
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condition<T extends Comparable<T>> {

    private String field;
    private String operator;
    private Expression expression;
    private T value;
    private List<T> values;

    public static <T extends Comparable<T>> Condition<? extends Comparable<?>> of(
            String field,
            String operator,
            Expression expression,
            String value,
            List<String> values,
            Function<String, T> converter)
    {
        return new Condition<>(
                field, operator, expression,
                value == null ? null : converter.apply(value),
                values == null ? Collections.emptyList() : values.stream().map(converter).collect(Collectors.toList())
        );
    }
}
