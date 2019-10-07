package com.example.spring.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class AppControllerAdvice {

	/**
	 * Form 入力時の初期設定.
	 * 
	 * @param binder {@link WebDataBinder} 
	 */
	@InitBinder
	public void initBuilder(WebDataBinder binder) {

		// 未入力時は null とする
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

	}
}
