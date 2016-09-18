package com.sdll18.springshoot.Servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sdll18.springshoot.Data.Json;
import com.sdll18.springshoot.Persistant.Album;
import com.sdll18.springshoot.Persistant.Photo;
import com.sdll18.springshoot.Service.AlbumService;
import com.sdll18.springshoot.Util.HttpResponseUtil;
import com.sdll18.springshoot.Util.MsgUtil;

@SuppressWarnings("serial")
public class CreateAlbumServlet extends MyServlet {

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
		String urls = data.get("urls");
		System.out.println(urls);
		String category = data.get("category");
		Album album = new Album();
		album.setCategory(category);
		album.setCoverUrl(urls.split(";")[0]);
		album.setCreateTime(String.valueOf(Calendar.getInstance()
				.getTimeInMillis()));
		album.setDescription("");
		album.setName("");
		for (String url : urls.split(";")) {
			Photo p = new Photo();
			p.setCreateTime(String.valueOf(Calendar.getInstance()
					.getTimeInMillis()));
			p.setName("");
			p.setOwnedAlbum(album);
			p.setUrl(url);
			album.getPhotos().add(p);
		}
		getAlbumService().create(album);
		HttpResponseUtil.returnMsg(resp,
				MsgUtil.getSuccessMsg("album has been created"));
	}
}
