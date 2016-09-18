package com.sdll18.springshoot.Service.Impl;

import java.util.List;

import com.sdll18.springshoot.Dao.AlbumDao;
import com.sdll18.springshoot.Persistant.Album;
import com.sdll18.springshoot.Service.AlbumService;

public class AlbumServiceImpl implements AlbumService {

	private static final int PAGE_SIZE = 6;

	private AlbumDao albumDao;

	public AlbumDao getAlbumDao() {
		return albumDao;
	}

	public void setAlbumDao(AlbumDao albumDao) {
		this.albumDao = albumDao;
	}

	@Override
	public void create(Album album) {
		getAlbumDao().create(album);
	}

	@Override
	public void remove(Album album) {
		getAlbumDao().remove(album);
	}

	@Override
	public void remove(int id) {
		getAlbumDao().remove(id);
	}

	@Override
	public void update(Album album) {
		getAlbumDao().update(album);
	}

	@Override
	public Album query(int id, boolean lazy) {
		return getAlbumDao().query(id, lazy);
	}

	@Override
	public List<Album> queryList(int page, boolean lazy) {
		return getAlbumDao().queryList(page, PAGE_SIZE, lazy);
	}

	@Override
	public List<Album> queryList(int page, int pageSize, boolean lazy) {
		return getAlbumDao().queryList(page, pageSize, lazy);
	}

	@Override
	public List<Album> queryList(String category, int page, boolean lazy) {
		return getAlbumDao().queryList(category, page, PAGE_SIZE, lazy);
	}

	@Override
	public List<Album> queryList(String category, int page, int pageSize,
			boolean lazy) {
		return getAlbumDao().queryList(category, page, pageSize, lazy);
	}

	@Override
	public List<Album> queryList(String category) {
		return getAlbumDao().queryList(category);
	}

}
