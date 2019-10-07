package com.example.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AppControllerConst.Page.INDEX)
public class IndexController {

	@GetMapping(path = AppControllerConst.Path.INDEX)
	public String index() {

		return AppControllerConst.Tpl.INDEX
				+ AppControllerConst.Page.INDEX;
	}
}
