package com.example.spring.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {
		UniqueDocumentUsersValidator.class,
		UniqueDocumentDefaultValidator.class
})
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDocument {

	String idField() default "id";

	String field();

	String message() default "{org.hibernate.validator.constraints.UniqueElements.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target(ElementType.TYPE_USE)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	public @interface List {
		UniqueDocument[] value(); // インターフェース名[] value()としておいてください
	}
}
