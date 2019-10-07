package com.example.spring.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.entity.Roles;
import com.example.spring.entity.User;
import com.example.spring.form.UserForm;
import com.example.spring.helper.UriHelper;
import com.example.spring.service.UserService;
import com.example.spring.validation.group.Create;
import com.example.spring.validation.group.Save;

@Controller
@RequestMapping(AppControllerConst.Uri.USERS)
public class UsersController {

	@Autowired
	protected UserService service;

	@ModelAttribute("roles")
	public Roles[] roles() {
		return Roles.values();
	}

	@ModelAttribute("user")
	public User user(@PathVariable(value = "id", required = false) String id) {
		if (StringUtils.isNotEmpty(id)) {
			return service.findById(id);
		}
		return new User();
	}

	/**
	 * index ページ用.
	 * 
	 * @param request リクエスト情報.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.INDEX)
	public String index(Model model, @PageableDefault(page = 0, size = 20) Pageable page) {
		model.addAttribute("users", service.findAll(page));
		return AppControllerConst.Tpl.USERS + AppControllerConst.Page.INDEX;

	}

	/**
	 * create ページ用 (GET).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.CREATE)
	public String create(@ModelAttribute("form") UserForm form) {
		return AppControllerConst.Tpl.USERS + AppControllerConst.Page.CREATE;
	}

	/**
	 * create ページ用 (POST).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @param result  バリデーション結果
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@PostMapping(path = AppControllerConst.Path.CREATE)
	public String create(
			@Validated(Create.class) @ModelAttribute("form") UserForm form,
			BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return AppControllerConst.Tpl.USERS + AppControllerConst.Page.CREATE;
		}
		User saveUser = service.insert(form);
		redirect.addFlashAttribute("success", "登録に成功しました。");
		return UriHelper.redirect(AppControllerConst.Uri.USERS + AppControllerConst.Path.MODIFY, saveUser.getId());
	}

	/**
	 * modify ページ用 (GET).
	 * 
	 * @param id      識別用ID
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.MODIFY)
	public String modify(@ModelAttribute("user") User user, @ModelAttribute("form") UserForm form) {
		return view(user, form, AppControllerConst.Tpl.USERS + AppControllerConst.Page.MODIFY);
	}

	/**
	 * modify ページ用 (POST).
	 * 
	 * @param id      識別用ID
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @param result  バリデーション結果
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@PostMapping(path = AppControllerConst.Path.MODIFY)
	public String modify(
			@ModelAttribute("user") User user,
			@Validated(Save.class) @ModelAttribute("form") UserForm form,
			BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return AppControllerConst.Tpl.USERS + AppControllerConst.Page.MODIFY;
		}
		User saveUser = service.save(form, user);
		redirect.addFlashAttribute("success", "更新に成功しました。");
		return UriHelper.redirect(AppControllerConst.Uri.USERS + AppControllerConst.Path.MODIFY, saveUser.getId());
	}

	/**
	 * view ページ用 (GET).
	 * 
	 * @param id      識別用ID
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.VIEW)
	public String view(@ModelAttribute("user") User user, @ModelAttribute("form") UserForm form) {
		return view(user, form, AppControllerConst.Tpl.USERS + AppControllerConst.Page.VIEW);
	}

	/**
	 * delete ページ用 (GET).
	 * 
	 * @param id      識別用ID
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.DELETE)
	public String delete(@ModelAttribute("user") User user, @ModelAttribute("form") UserForm form) {
		return view(user, form, AppControllerConst.Tpl.USERS + AppControllerConst.Page.DELETE);
	}

	protected String view(User user, UserForm form, String tplPath) {
		BeanUtils.copyProperties(user, form);
		return tplPath;
	}

	/**
	 * delete ページ用 (POST/DELETE).
	 * 
	 * @param id 識別用ID
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@PostMapping(path = AppControllerConst.Path.DELETE)
	@DeleteMapping(path = AppControllerConst.Path.DELETE)
	public String delete(@ModelAttribute("user") User user, RedirectAttributes redirect) {
		service.delete(user);
		redirect.addFlashAttribute("success", "削除に成功しました。");
		return UriHelper.redirect(AppControllerConst.Uri.USERS + AppControllerConst.Path.INDEX);
	}
}
