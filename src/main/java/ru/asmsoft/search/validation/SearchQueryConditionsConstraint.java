package ru.asmsoft.search.validation;

import ru.asmsoft.search.model.FieldType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = SearchQueryConditionsValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchQueryConditionsConstraint {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String message() default "Invalid conditions fields specified";
    String field();
    String operator();
    FieldType type();

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface of {
        SearchQueryConditionsConstraint[] value();
    }
}
