package com.sdll18.umedia.Servlet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.sdll18.umedia.Data.SuperMap;

@SuppressWarnings("serial")
public class UpLoadImageServlet extends HttpServlet {

	private static final String STATIC_PATH = "www.sdll18.com";

	// private String tmp = "/Users/SDLL18/Desktop";

	private static final String SAVE_PATH = "/UMediaData";

	// private static final String STATIC_PATH_DEBUG = "localhost:8080";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory(4 * 1024 * 1024,
				new File("./temp"));
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(req);
			for (FileItem item : items) {
				if (!item.isFormField()) {
					String contentType = item.getContentType();
					String endName = contentType.substring(contentType
							.lastIndexOf("/") + 1);
					// long size = item.getSize();
					// System.out.println("receiving file : size:" + size
					// + "\nbyte name:" + fileName);
					// System.out.println(contentType);
					// System.out.println(endName);
					String name = new Date().getTime() + "." + endName;
					File tmpSaveFile = new File(SAVE_PATH + "/tmpfile/"
							+ contentType.split("/")[0] + "/" + name);
					if (!tmpSaveFile.getParentFile().exists()) {
						tmpSaveFile.getParentFile().mkdirs();
					}
					item.write(tmpSaveFile);
					BufferedImage bi = ImageIO.read(tmpSaveFile);
					BufferedImage tmp = new BufferedImage(140, 140,
							BufferedImage.TYPE_INT_RGB);
					Graphics g = tmp.createGraphics();
					g.drawImage(bi, 0, 0, 140, 140, null);
					g.dispose();
					File saveFile = new File(SAVE_PATH + "/file/"
							+ contentType.split("/")[0] + "/" + name);
					if (!saveFile.getParentFile().exists()) {
						saveFile.getParentFile().mkdirs();
					}
					ImageIO.write(tmp, "jpg", saveFile);
					tmpSaveFile.delete();
					returnMsg(
							resp,
							new SuperMap()
									.put("state", "sucess")
									.put("url",
											"http://"
													+ STATIC_PATH
													+ "/UMediaService/downimage?filename="
													+ name).finishByJson());
				} else {
					// do nothing
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(404);
		}
	}

	/**
	 * return message through httpResponse
	 * 
	 * @param msg
	 */
	private void returnMsg(HttpServletResponse resp, String msg) {
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
}
