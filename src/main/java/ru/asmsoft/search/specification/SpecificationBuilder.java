package ru.asmsoft.search.specification;

import ru.asmsoft.search.model.Condition;
import ru.asmsoft.search.model.SearchCondition;
import ru.asmsoft.search.model.SearchQuery;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpecificationBuilder<T> {

    private final Class<T> entityClass;
    private final List<Condition<? extends Comparable<?>>> conditions = new ArrayList<>();

    public SpecificationBuilder() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public SpecificationBuilder<T> withCondition(Condition<? extends Comparable<?>> condition){
        conditions.add(condition);
        return this;
    }

    public SpecificationBuilder<T> withConditions(List<Condition<? extends Comparable<?>>> conditions) {
        this.conditions.addAll(conditions);
        return this;
    }

    public Specification<T> build(SearchQuery query) {
        final Map<String, Class<?>> fields = extractFields();
        conditions.addAll(query.getConditions().stream()
                .filter(condition -> fields.containsKey(condition.getField()))
                .map(condition -> condition.into(fields.get(condition.getField())))
                .collect(Collectors.toList()));
        return new CustomSpecification<>(conditions);
    }

    private Map<String, Class<?>> extractFields() {
        return Arrays.stream(entityClass.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Field::getType));
    }
}
