package com.java.recruitment.validation.line;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CriteriaJsonValidator implements ConstraintValidator<ValidCriteriaJson, String> {

    @Override
    public boolean isValid(String criteriaJson, ConstraintValidatorContext context) {
        if (criteriaJson == null || criteriaJson.isEmpty()) {
            return true;
        }

        return !criteriaJson.contains("recruiter.id") && !criteriaJson.contains("hr.id");
    }
}
