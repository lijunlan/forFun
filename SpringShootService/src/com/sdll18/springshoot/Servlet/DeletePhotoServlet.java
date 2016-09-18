package com.sdll18.springshoot.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdll18.springshoot.Data.Json;
import com.sdll18.springshoot.Service.PhotoService;
import com.sdll18.springshoot.Util.HttpResponseUtil;
import com.sdll18.springshoot.Util.MsgUtil;

@SuppressWarnings("serial")
public class DeletePhotoServlet extends MyServlet {

	private PhotoService photoService;

	public PhotoService getPhotoService() {
		return photoService;
	}

	public void setPhotoService(PhotoService photoService) {
		this.photoService = photoService;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setPhotoService((PhotoService) getApplicationContext().getBean(
				"photoService"));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String json = Json.getJson(req);
		Map<String, String> data = Json.getMap(json);
		int id = Integer.parseInt(data.get("id"));
		getPhotoService().remove(id);
		HttpResponseUtil.returnMsg(resp,
				MsgUtil.getSuccessMsg("The photo has been deleted"));
	}
}
