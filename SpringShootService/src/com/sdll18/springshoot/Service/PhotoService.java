package com.sdll18.springshoot.Service;

import java.util.List;

import com.sdll18.springshoot.Persistant.Photo;

public interface PhotoService {

	void create(Photo photo);

	void remove(Photo photo);

	void remove(int id);

	void update(Photo photo);

	Photo query(int id);

	List<Photo> queryList(int page);

	List<Photo> queryList(int page, int pageSize);

	List<Photo> queryListByAlbum(int albumid);
}
