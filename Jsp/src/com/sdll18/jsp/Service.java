package com.sdll18.jsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Service extends HttpServlet {
	public HttpSession hsession;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("get!");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("username:" + req.getParameter("username"));
		System.out.println("password:" + req.getParameter("password"));
		if (req.getParameter("username").equals("li")
				&& req.getParameter("password").equals("123")) {
			hsession = req.getSession();
			hsession.setAttribute("username", req.getParameter("username"));
			resp.sendRedirect(resp.encodeRedirectURL("Welcome.jsp"));
			System.out.println(resp.encodeRedirectURL("Welcome.jsp"));
		} else {
			resp.sendRedirect(resp.encodeRedirectURL("Hello.jsp"));
			System.out.println(resp.encodeRedirectURL("Hello.jsp"));
		}
	}
}
