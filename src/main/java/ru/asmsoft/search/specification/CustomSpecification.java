package ru.asmsoft.search.specification;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.query.Criteria;
import ru.asmsoft.search.model.Condition;
import ru.asmsoft.search.model.Operations;

/**
 * Custom specification.
 *
 * @param <T> type
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CustomSpecification<T> {

  /** Conditions list. */
  private final List<Condition<? extends Comparable<?>>> conditions;

  /**
   * Build criteria from parameters.
   *
   * @param condition condition to handle
   * @return Criteria
   * @param <E> the type of Condition
   */
  private static <E extends Comparable<E>> Criteria fromCondition(Condition<E> condition) {
    return switch (condition.getOperator()) {
      case Operations.EQUALS -> Criteria.where(condition.getField()).is(condition.getValue());
      case Operations.NOT_EQUALS -> Criteria.where(condition.getField()).not(condition.getValue());
      case Operations.LIKE -> Criteria.where(condition.getField()).like((String) condition.getValue());
      case Operations.GREATER -> Criteria.where(condition.getField()).greaterThan(condition.getValue());
      case Operations.LESS -> Criteria.where(condition.getField()).lessThan(condition.getValue());
      case Operations.GREATER_OR_EQUALS ->
          Criteria.where(condition.getField()).greaterThanOrEquals(condition.getValue());
      case Operations.LESS_OR_EQUALS ->
          Criteria.where(condition.getField()).lessThanOrEquals(condition.getValue());
      case Operations.IN -> Criteria.where(condition.getField()).in(condition.getValues());
      default -> Criteria.empty();
    };
  }

  /**
   * {@inheritDoc}
   */
  public Criteria toCriteria() {
    final AtomicReference<Criteria> result = new AtomicReference<>(Criteria.empty());
    conditions.stream()
        .filter(
            condition ->
                condition != null
                    && condition.getField() != null
                    && condition.getOperator() != null
                    && (condition.getValue() != null || condition.getValues() != null))
        .forEach(
            condition -> {
              Criteria criteria = fromCondition(condition);
              switch (condition.getExpression()) {
                case AND:
                  result.set(result.get().and(criteria));
                  break;
                case OR:
                  result.set(result.get().or(criteria));
                  break;
                default:
                  // NOP
              }
            });
    return result.get();
  }
}
