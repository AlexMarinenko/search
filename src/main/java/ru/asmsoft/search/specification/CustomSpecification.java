package ru.asmsoft.search.specification;

import ru.asmsoft.search.model.Condition;
import ru.asmsoft.search.model.Operations;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@RequiredArgsConstructor
public class CustomSpecification<T> implements Specification<T> {

    private final List<Condition<? extends Comparable<?>>> conditions;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        final  AtomicReference<Predicate> result = new AtomicReference<>(builder.conjunction());
        conditions.stream()
            .filter(condition ->
                    condition != null &&
                    condition.getField() != null &&
                    condition.getOperator() != null &&
                    ( condition.getValue() != null || condition.getValues() != null )
            )
            .forEach(condition -> {
                Predicate p = fromCondition(root, builder, condition);
                switch (condition.getExpression()) {
                    case AND:
                        result.set(builder.and(result.get(), p));
                        break;
                    case OR:
                        result.set(builder.or(result.get(), p));
                        break;
                    default:
                        // NOP
                }
            });
        return result.get();
    }

    private static <T,E extends Comparable<E>> Predicate fromCondition(Root<T> root, CriteriaBuilder builder, Condition<E> condition) {
        switch(condition.getOperator()) {
            case Operations.EQUALS:
                return builder.equal(root.get(condition.getField()), condition.getValue());
            case Operations.NOT_EQUALS:
                return builder.notEqual(root.get(condition.getField()), condition.getValue());
            case Operations.LIKE:
                return builder.like(root.get(condition.getField()), (String)condition.getValue());
            case Operations.GREATER:
                return builder.greaterThan(root.get(condition.getField()), condition.getValue());
            case Operations.LESS:
                return builder.lessThan(root.get(condition.getField()), condition.getValue());
            case Operations.GREATER_OR_EQUALS:
                return builder.greaterThanOrEqualTo(root.get(condition.getField()), condition.getValue());
            case Operations.LESS_OR_EQUALS:
                return builder.lessThanOrEqualTo(root.get(condition.getField()), condition.getValue());
            case Operations.IN:
                return root.get(condition.getField()).in(condition.getValues());
            default:
                return null;
        }
    }
}
