package com.example.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.entity.Roles;
import com.example.spring.entity.User;
import com.example.spring.form.UserForm;
import com.example.spring.helper.UriHelper;
import com.example.spring.service.UserService;
import com.example.spring.validation.group.Create;

@Controller
@RequestMapping(AppControllerConst.Uri.SIGNUP)
public class SignupController {

	@Autowired
	protected UserService service;

	@ModelAttribute("roles")
	public Roles[] roles() {
		return Roles.values();
	}

	@ModelAttribute("user")
	public User user() {
		return new User();
	}

	/**
	 * create ページ用 (GET).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.INDEX)
	public String create(Model model, @ModelAttribute("form") UserForm form) {
		return AppControllerConst.Tpl.SIGNUP
				+ AppControllerConst.Page.INDEX;
	}

	/**
	 * create ページ用 (POST).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @param result  バリデーション結果
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@PostMapping(path = AppControllerConst.Path.INDEX)
	public String create(Model model,
			@Validated(Create.class) @ModelAttribute("form") UserForm form,
			BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return AppControllerConst.Tpl.SIGNUP
					+ AppControllerConst.Page.INDEX;
		}
		User saveUser = service.insert(form);
		redirect.addFlashAttribute("success", "登録に成功しました。");

		if (saveUser.isEnabled()) {
			SecurityContextHolder.getContext().setAuthentication(
					new UsernamePasswordAuthenticationToken(
							form.getUsername(), form.getPassword(), saveUser.getAuthorities()));

			return UriHelper.redirect(AppControllerConst.Uri.INDEX);
		} else {
			return UriHelper.redirect(AppControllerConst.Uri.LOGIN);
		}
	}

}
