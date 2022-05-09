package com.smartos.search.specification;

import com.smartos.search.model.Condition;
import com.smartos.search.model.SearchCondition;
import com.smartos.search.model.SearchQuery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationBuilder {

    private final List<Condition<? extends Comparable<?>>> conditions = new ArrayList<>();

    public SpecificationBuilder withCondition(Condition<? extends Comparable<?>> condition){
        conditions.add(condition);
        return this;
    }

    public SpecificationBuilder withConditions(List<Condition<? extends Comparable<?>>> conditions) {
        this.conditions.addAll(conditions);
        return this;
    }

    public <T> Specification<T> build(SearchQuery query) {
        conditions.addAll(query.getConditions().stream()
                .map(SearchCondition::into)
                .collect(Collectors.toList()));
        return new CustomSpecification<>(conditions);
    }
}
