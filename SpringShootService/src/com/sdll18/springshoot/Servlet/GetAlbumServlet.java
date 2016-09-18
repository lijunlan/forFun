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
import com.sdll18.springshoot.Persistant.Album;
import com.sdll18.springshoot.Service.AlbumService;
import com.sdll18.springshoot.Util.HttpResponseUtil;

@SuppressWarnings("serial")
public class GetAlbumServlet extends MyServlet {

	private AlbumService albumService;

	public AlbumService getAlbumService() {
		return albumService;
	}

	public void setAlbumService(AlbumService albumService) {
		this.albumService = albumService;
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setAlbumService((AlbumService) getApplicationContext().getBean(
				"albumService"));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String json = Json.getJson(req);
		Map<String, String> data = Json.getMap(json);
		String category = data.get("category");
		List<Album> albums = getAlbumService().queryList(category);
		List<Map<String, String>> toSend = new ArrayList<Map<String, String>>();
		for (Album a : albums) {
			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put("url", a.getCoverUrl());
			tmp.put("id", "" + a.getId());
			toSend.add(tmp);
		}
		HttpResponseUtil.returnMsg(resp, Json.getJsonFromListMap(toSend));
	}
}
