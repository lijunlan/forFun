package com.sdll18.springshoot.Servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sdll18.springshoot.Persistant.User;
import com.sdll18.springshoot.Service.UserService;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	private ApplicationContext applicationContext;

	private UserService userService;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setApplicationContext(WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()));
		setUserService((UserService) getApplicationContext().getBean(
				"userService"));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// System.out.println("get!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		User user = getUserService().query(username);
		if (user == null) {
			resp.sendRedirect(resp
					.encodeRedirectURL("/SpringShootService/page/login.html"));
			return;
		}
		if (user.getPassword().equals(password)) {
			Cookie cookie = new Cookie("user_id", String.valueOf(user.getId()));
			resp.addCookie(cookie);
			resp.sendRedirect(resp
					.encodeRedirectURL("/SpringShootService/page/manage.html"));
		} else {
			resp.sendRedirect(resp
					.encodeRedirectURL("/SpringShootService/page/login.html"));
		}
	}
}
