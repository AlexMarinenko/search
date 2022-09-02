package ru.asmsoft.search.specification;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import ru.asmsoft.search.model.Condition;
import ru.asmsoft.search.model.SearchQuery;

/**
 * Specification builder.
 */
public class SpecificationBuilder<T> {

  private final Class<T> entityClass;
  private final List<Condition<? extends Comparable<?>>> conditions = new ArrayList<>();

  /**
   * Specification builder constructor.
   *
   * @param entityClass the class of entity
   */
  public SpecificationBuilder(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  /**
   * Add a new condition to the chain.
   *
   * @param condition to add
   * @return SpecificationBuilder object with added Condition
   */
  public SpecificationBuilder<T> withCondition(Condition<? extends Comparable<?>> condition) {
    conditions.add(condition);
    return this;
  }

  /**
   * Add a list of new conditions to the chain.
   *
   * @param conditions to add
   * @return SpecificationBuilder object with added Conditions
   */
  public SpecificationBuilder<T> withConditions(
      List<Condition<? extends Comparable<?>>> conditions) {
    this.conditions.addAll(conditions);
    return this;
  }

  /**
   * Building specification for Search query.
   *
   * @param query the search query
   * @return Specification
   */
  public Specification<T> build(SearchQuery query) {
    final Map<String, Class<?>> fields = extractFields();
    if (query.getConditions() != null) {
      conditions.addAll(
          query.getConditions().stream()
              .filter(condition -> fields.containsKey(condition.getField()))
              .map(condition ->
                      condition.into(fields.get(condition.getField())))
              .toList());
    }
    return new CustomSpecification<>(conditions);
  }

  private Map<String, Class<?>> extractFields() {
    return Arrays.stream(entityClass.getDeclaredFields())
        .collect(Collectors.toMap(Field::getName, Field::getType));
  }
}
