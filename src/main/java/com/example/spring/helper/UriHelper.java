package com.example.spring.helper;

import org.springframework.web.util.UriComponentsBuilder;

public final class UriHelper {

	public static final String path(String path, Object... uriVariables) {
		return UriComponentsBuilder.fromPath(path).build(uriVariables).toASCIIString();
	}

	public static final String redirect(String path, Object... uriVariables) {
		return "redirect:" + path(path, uriVariables);
	}
}
