package com.sdll18.springshoot.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdll18.springshoot.Data.Json;
import com.sdll18.springshoot.Persistant.Photo;
import com.sdll18.springshoot.Service.PhotoService;
import com.sdll18.springshoot.Util.HttpResponseUtil;

@SuppressWarnings("serial")
public class GetPhotoServlet extends MyServlet {

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
		int id = Integer.parseInt(data.get("album"));
		List<Photo> photos = getPhotoService().queryListByAlbum(id);
		List<Map<String, String>> toSend = new ArrayList<Map<String, String>>();
		for (Photo p : photos) {
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("url", p.getUrl());
			tmp.put("id", "" + p.getId());
			toSend.add(tmp);
		}
		HttpResponseUtil.returnMsg(resp, Json.getJsonFromListMap(toSend));
	}
}
