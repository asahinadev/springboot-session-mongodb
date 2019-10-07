package com.example.spring.controller;

public class AppControllerConst {

	public static class Path {
		public static final String INDEX = "";
		public static final String CREATE = "/create";
		public static final String MODIFY = "/modify/{id}";
		public static final String DELETE = "/delete/{id}";
		public static final String VIEW = "/view/{id}";
	}

	public static class Page {
		public static final String LOGIN = "login";
		public static final String INDEX = "index";
		public static final String CREATE = "create";
		public static final String MODIFY = "modify";
		public static final String DELETE = "delete";
		public static final String VIEW = "view";
	}

	public static class Uri {
		public static final String INDEX = "/";
		public static final String LOGIN = "/login";
		public static final String LOGOUT = "/logout";
		public static final String ERROR = "/error";
		public static final String IMAGES = "/images";
		public static final String JSCRIPT = "/js";
		public static final String STYLE = "/css";
		public static final String WEBJARS = "/webjars";

		public static final String USERS = "/users";
		public static final String SIGNUP = "/signup";
	}

	public static class Tpl {
		public static final String INDEX = "";
		public static final String LOGIN = "";
		public static final String USERS = "users/";
		public static final String SIGNUP = "signup/";
	}

}
