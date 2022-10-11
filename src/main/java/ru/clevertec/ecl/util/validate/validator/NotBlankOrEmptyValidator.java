package ru.clevertec.ecl.util.validate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ru.clevertec.ecl.util.validate.constraint.NotBlankOrNull;

public class NotBlankOrEmptyValidator implements ConstraintValidator<NotBlankOrNull, String> {

    @Override
    public void initialize(NotBlankOrNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.trim().isEmpty();
    }
}
