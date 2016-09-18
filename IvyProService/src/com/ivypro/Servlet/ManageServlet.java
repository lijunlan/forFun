package com.ivypro.Servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ivypro.Handle.MessageHandle;

@SuppressWarnings("serial")
public class ManageServlet extends HttpServlet {

	private ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setApplicationContext(WebApplicationContextUtils
				.getRequiredWebApplicationContext(getServletContext()));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// System.out.println("get!");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MessageHandle.deal(req, resp, getApplicationContext());
	}

}