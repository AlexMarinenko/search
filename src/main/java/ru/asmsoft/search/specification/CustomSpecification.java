package ru.asmsoft.search.specification;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
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
   * Build Criteria from parameters.
   *
   * @param condition condition to handle
   * @return Criteria
   * @param <E> the type of Condition
   */
  private static <E extends Comparable<E>> Criteria fromCondition(Condition<E> condition) {
    switch (condition.getOperator()) {
      case Operations.EQUALS:
        return Criteria.where(condition.getField()).is(condition.getValue());
      case Operations.NOT_EQUALS:
        return Criteria.where(condition.getField()).not(condition.getValue());
      case Operations.LIKE:
        return Criteria.where(condition.getField()).like(condition.getValue().toString());
      case Operations.GREATER:
        return Criteria.where(condition.getField()).greaterThan(condition.getValue());
      case Operations.LESS:
        return Criteria.where(condition.getField()).lessThan(condition.getValue());
      case Operations.GREATER_OR_EQUALS:
        return Criteria.where(condition.getField()).greaterThanOrEquals(condition.getValue());
      case Operations.LESS_OR_EQUALS:
        return Criteria.where(condition.getField()).lessThanOrEquals(condition.getValue());
      case Operations.IN:
        return Criteria.where(condition.getField()).in(condition.getValues());
      default:
        return null;
    }
  }

  /**
   * Create {@link CriteriaDefinition} based on provided conditions.
   *
   * @return built {@link CriteriaDefinition}
   */
  public CriteriaDefinition toCriteria() {
    final AtomicReference<Criteria> result = new AtomicReference<>();
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
              if (criteria == null) {
                return;
              }

              if (result.get() == null) {
                result.set(criteria);
                return;
              }

              Criteria current = result.get();
              switch (condition.getExpression()) {
                case AND:
                  result.set(current.and(criteria));
                  break;
                case OR:
                  result.set(current.or(criteria));
                  break;
                default:
                  // NOP
              }
            });

    return Objects.requireNonNullElseGet(result.get(), Criteria::empty);
  }
}
