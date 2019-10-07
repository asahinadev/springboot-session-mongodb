package com.example.spring.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueDocumentDefaultValidator implements ConstraintValidator<UniqueDocument, Object> {

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		return false;
	}

}
