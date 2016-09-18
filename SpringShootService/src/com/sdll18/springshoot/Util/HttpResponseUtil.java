package com.sdll18.springshoot.Util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class HttpResponseUtil {
	/**
	 * return message through httpResponse
	 * 
	 * @param msg
	 */
	public static void returnMsg(HttpServletResponse resp, String msg) {
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		try {
			OutputStream stream = resp.getOutputStream();
			System.out.println(msg);
			stream.write(msg.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * return error message
	 * 
	 * @param msg
	 */
	public static void returnError(HttpServletResponse resp, String msg) {
		returnMsg(resp, msg);
	}
}
