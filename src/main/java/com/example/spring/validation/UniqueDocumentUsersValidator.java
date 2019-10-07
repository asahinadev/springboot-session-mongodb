package com.example.spring.validation;

import java.util.NoSuchElementException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.spring.entity.User;
import com.example.spring.form.UserForm;
import com.example.spring.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UniqueDocumentUsersValidator implements ConstraintValidator<UniqueDocument, UserForm> {

	@Autowired
	UserService userService;

	UniqueDocument annotation;

	@Override
	public void initialize(UniqueDocument annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isValid(UserForm form, ConstraintValidatorContext context) {

		User data;

		log.debug("form => {}", form);

		try {
			switch (this.annotation.field()) {
			case "username":
				data = userService.loadUserByUsername(form.getUsername());
				break;

			case "email":
				data = userService.loadUserByUsername(form.getEmail());
				break;

			default:
				log.error("default");
				error(this.annotation.field(), context, this.annotation.message());
				return false;
			}

		} catch (NoSuchElementException e) {
			log.info("empty");
			log.debug(e.getMessage(), e);
			return true;

		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			error(this.annotation.field(), context, this.annotation.message());
			return false;

		}
		log.debug("data => {}", data);
		if (data == null) {
			return true;
		}
		if (!StringUtils.equals(data.getId(), form.getId())) {
			error(this.annotation.field(), context, this.annotation.message());
			return false;
		}
		return true;
	}

	protected boolean error(String field, ConstraintValidatorContext context, String message) {

		context.disableDefaultConstraintViolation();
		ConstraintViolationBuilder builder = context.buildConstraintViolationWithTemplate(message);
		if (field != null) {
			builder.addPropertyNode(field).addConstraintViolation();
		} else {
			builder.addConstraintViolation();
		}
		return false;
	}
}
