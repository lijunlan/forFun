package com.sdll18.springshoot.Service.Impl;

import java.util.List;

import com.sdll18.springshoot.Dao.PhotoDao;
import com.sdll18.springshoot.Persistant.Photo;
import com.sdll18.springshoot.Service.PhotoService;

public class PhotoServiceImpl implements PhotoService {

	private static final int PAGE_SIZE = 6;

	private PhotoDao photoDao;

	public PhotoDao getPhotoDao() {
		return photoDao;
	}

	public void setPhotoDao(PhotoDao photoDao) {
		this.photoDao = photoDao;
	}

	@Override
	public void create(Photo photo) {
		getPhotoDao().create(photo);
	}

	@Override
	public void remove(Photo photo) {
		getPhotoDao().remove(photo);
	}

	@Override
	public void remove(int id) {
		getPhotoDao().remove(id);
	}

	@Override
	public void update(Photo photo) {
		getPhotoDao().update(photo);
	}

	@Override
	public Photo query(int id) {
		return getPhotoDao().query(id);
	}

	@Override
	public List<Photo> queryList(int page) {
		return getPhotoDao().queryList(page, PAGE_SIZE);
	}

	@Override
	public List<Photo> queryList(int page, int pageSize) {
		return getPhotoDao().queryList(page, pageSize);
	}

	@Override
	public List<Photo> queryListByAlbum(int albumid) {
		return getPhotoDao().queryList(albumid);
	}

}
