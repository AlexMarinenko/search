package ru.asmsoft.search.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import ru.asmsoft.search.model.FieldType;

/**
 * Search query conditions constraint.
 */
@Documented
@Constraint(validatedBy = SearchQueryConditionsValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchQueryConditionsConstraint {

  /** Group of conditions.
   *
   * @return groups
   */
  Class<?>[] groups() default {};

  /** Payload.
   *
   * @return payloads
   */
  Class<? extends Payload>[] payload() default {};

  /** Validation message.
   *
   * @return validation message
   */
  String message() default "Invalid conditions fields specified";

  /** The field to apply this validation to.
   *
   * @return the field
   */
  String field();

  /** Operator for the field.
   *
   * @return operator
   */
  String operator();

  /** The type of the field.
   *
   * @return field type
   */
  FieldType type();

  /**
   * Construct the constraints.
   */
  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  @interface Of {
    /** Search query conditions constraint.
     *
     * @return search constraints
     */
    SearchQueryConditionsConstraint[] value();
  }

}
