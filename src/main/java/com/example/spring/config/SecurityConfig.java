package com.example.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.spring.controller.AppControllerConst;
import com.example.spring.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * HOME PAGE.
	 */
	public static final String HOME_PAGE = AppControllerConst.Uri.INDEX;

	/**
	 * LOGIN PAGE.
	 */
	public static final String LOGIN_PAGE = AppControllerConst.Uri.LOGIN;

	/**
	 * LOGIN AREA.
	 */
	public static final String LOGIN_AREA = LOGIN_PAGE + "/**";

	/**
	 * LOGOUT AREA.
	 */
	public static final String LOGOUT_AREA = AppControllerConst.Uri.LOGOUT + "/**";

	/**
	 * ERROR AREA.
	 */
	public static final String ERROR_AREA = AppControllerConst.Uri.ERROR + "/**";

	/**
	 * USER SIGNUP AREA
	 */
	public static final String SIGNUP_AREA = AppControllerConst.Uri.SIGNUP + "/**";

	/**
	 * USER NAME PARAMETER.
	 */
	public static final String PARAM_USERNAME = "username";

	/**
	 * USER PASSWORD PARAMETER.
	 */
	public static final String PARAM_PASSWORD = "password";

	@Autowired
	protected UserService userService;

	/**
	 * Password Encoder.
	 * 
	 * @return {@link BCryptPasswordEncoder}.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		// 静的ファイルを許可する
		web.ignoring().antMatchers(
				AppControllerConst.Uri.WEBJARS + "/**",
				AppControllerConst.Uri.ERROR + "/**",
				AppControllerConst.Uri.STYLE + "/**",
				AppControllerConst.Uri.JSCRIPT + "/**",
				AppControllerConst.Uri.IMAGES + "/**",
				"/favicon.ico");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// 認可の設定
		http.antMatcher("/**").authorizeRequests()

				// エラーページ関連、ホームページ、サインアップ関連
				.antMatchers(ERROR_AREA).permitAll()
				.antMatchers(HOME_PAGE).permitAll()
				.antMatchers(SIGNUP_AREA).permitAll()

				// その他ページ郡
				.anyRequest().authenticated();

		// ログイン設定
		http.formLogin()
				.loginProcessingUrl(LOGIN_PAGE)
				.loginPage(LOGIN_PAGE)

				.failureUrl(LOGIN_PAGE + "?error")
				.defaultSuccessUrl(HOME_PAGE, true)

				.usernameParameter(PARAM_USERNAME)
				.passwordParameter(PARAM_PASSWORD)
				.permitAll();

		// ログアウト設定
		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_AREA))
				.logoutSuccessUrl(LOGIN_PAGE + "?logout");

		// リメンバーミー
		http.rememberMe();
		/**/

	}

	@Override
	protected void configure(AuthenticationManagerBuilder authentication) throws Exception {

		// その他 認証設定を無効化する
		super.configure(authentication);

		// 認証サービス／パスワードエンコーダーを設定する
		authentication
				// User Details Service
				.userDetailsService(userService)
				// Password Encoder
				.passwordEncoder(passwordEncoder());
	}

}
