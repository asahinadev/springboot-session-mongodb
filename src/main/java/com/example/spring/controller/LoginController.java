package com.example.spring.controller;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.form.UserForm;
import com.example.spring.helper.UriHelper;

@Controller
@RequestMapping(AppControllerConst.Uri.LOGIN)
public class LoginController {

	/**
	 * login ページ用（正常時）.
	 * 
	 * @param request リクエスト情報
	 * @param form    入力フォーム
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.INDEX)
	public String login(@ModelAttribute("form") UserForm form) {
		return AppControllerConst.Tpl.LOGIN
				+ AppControllerConst.Page.LOGIN;
	}

	/**
	 * login ページ用（異常時）.
	 * 
	 * @param request   リクエスト情報
	 * @param form      入力フォーム
	 * @param exception 認証エラー
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(path = AppControllerConst.Path.INDEX, params = "error")
	public String login(
			@ModelAttribute("form") UserForm form,
			@SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception,
			RedirectAttributes attributes) {

		if (exception == null) {
			attributes.addFlashAttribute("message", "不明なエラーです");
		} else {
			if (exception instanceof DisabledException) {
				attributes.addFlashAttribute("message", "アカウントが無効です");
			} else if (exception instanceof LockedException) {
				attributes.addFlashAttribute("message", "アカウントが凍結中です");
			} else if (exception instanceof AccountExpiredException) {
				attributes.addFlashAttribute("message", "アカウントの有効期限が切れています");
			} else if (exception instanceof CredentialsExpiredException) {
				attributes.addFlashAttribute("message", "パスワードの有効期限が切れています");
			} else if (exception instanceof BadCredentialsException) {
				attributes.addFlashAttribute("message", "アカウントまたはパスワードが一致しませんでした");
			} else if (exception instanceof UsernameNotFoundException) {
				attributes.addFlashAttribute("message", "アカウントまたはパスワードが一致しませんでした");
			} else {
				attributes.addFlashAttribute("message", exception.getMessage());
			}
		}
		return UriHelper.redirect(AppControllerConst.Uri.LOGIN);
	}

	@GetMapping(path = AppControllerConst.Path.INDEX, params = "logout")
	public String logout(@ModelAttribute("form") UserForm form, RedirectAttributes attributes) {
		attributes.addAttribute("message", "ログアウトが成功しました。");
		return UriHelper.redirect(AppControllerConst.Uri.LOGIN);
	}
}
