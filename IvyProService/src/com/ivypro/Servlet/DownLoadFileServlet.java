package com.ivypro.Servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ivypro.Util.ConfigurationXmlUtil;

@SuppressWarnings("serial")
public class DownLoadFileServlet extends HttpServlet {

	// private static final String SAVE_PATH = "/UMediaData";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String filename = req.getParameter("filename");
		// String id = req.getParameter("kind");
		filename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
		try {
			File file = new File(ConfigurationXmlUtil.getInstance()
					.getSettingData().get("cachePath")
					+ "/Document/" + filename);
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			resp.reset();
			// 设置response的Header
			resp.addHeader("Content-Disposition", "attachment;filename="
					+ filename);
			resp.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					resp.getOutputStream());
			resp.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
