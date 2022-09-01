package ru.asmsoft.search.validation;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.GenericValidator;
import ru.asmsoft.search.model.FieldType;
import ru.asmsoft.search.model.SearchQuery;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.lang3.time.DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT;
import static org.apache.commons.lang3.time.DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT;


public class SearchQueryConditionsValidator implements ConstraintValidator<SearchQueryConditionsConstraint, SearchQuery> {

    private String field;
    private String operator;
    private FieldType type;

    public void initialize(SearchQueryConditionsConstraint constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.operator = constraintAnnotation.operator();
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(SearchQuery searchQuery, ConstraintValidatorContext constraintValidatorContext) {
        if (searchQuery.getConditions() == null) {
            return false;
        }
        return searchQuery.getConditions().stream()
                .anyMatch(sc ->
                        field.equals(sc.getField()) &&
                                operator.equals(sc.getOperator()) &&
                                valueTypeIsCorrect(String.valueOf(sc.getValue())));


    }

    private boolean valueTypeIsCorrect(String value) {
        return switch (type) {
            case BOOLEAN -> BooleanUtils.toBooleanObject(value) != null;
            case BIGDECIMAL, BIGINTEGER, DOUBLE, FLOAT, LONG, SHORT, INTEGER -> NumberUtils.isCreatable(value);
            case DATE -> GenericValidator.isDate(value, ISO_8601_EXTENDED_DATE_FORMAT.getPattern(), true);
            case DATETIME -> GenericValidator.isDate(value, ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern(), true);
            default -> true;
        };
    }
}
