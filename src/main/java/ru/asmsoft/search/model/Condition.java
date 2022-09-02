package ru.asmsoft.search.model;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Search condition for specification.
 *
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

  /**
   * Condition constructor.
   *
   * @param field the field for condition
   * @param operator operator should be applied
   * @param expression expression should be added
   * @param value single value for the condition
   * @param values a list of values for such conditions like IN
   * @param converter converter function
   * @param <T> type
   * @return Constructed condition
   */
  public static <T extends Comparable<T>> Condition<? extends Comparable<?>> of(
      String field,
      String operator,
      Expression expression,
      String value,
      List<String> values,
      Function<String, T> converter) {
    return new Condition<>(
        field,
        operator,
        expression,
        value == null ? null : converter.apply(value),
        values == null
            ? Collections.emptyList()
            : values.stream().map(converter).toList());
  }
}
