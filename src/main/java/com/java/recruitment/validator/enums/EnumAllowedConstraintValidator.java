package com.java.recruitment.validator.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumAllowedConstraintValidator implements ConstraintValidator<EnumAllowedConstraint, Enum<?>> {
    private Set<String> allowedValues;
    private Set<String> enumValues;

    @Override
    public void initialize(final EnumAllowedConstraint constraint) {
        this.allowedValues = Arrays.stream(constraint.allowed()).collect(Collectors.toSet());
        this.enumValues = Stream.of(constraint.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(final Enum<?> value, final ConstraintValidatorContext context) {
        return value == null || enumValues.contains(value.toString()) && allowedValues.contains(value.toString());
    }
}
