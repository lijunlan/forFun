package com.sdll18.springshoot.Dao;

import java.util.List;

import com.sdll18.springshoot.Persistant.Photo;

public interface PhotoDao {

	void create(Photo photo);

	void remove(Photo photo);

	void remove(int id);

	void update(Photo photo);

	void updateFromSql(String sql);

	Photo query(int id);

	List<Photo> queryList(int page, int pageSize);

	List<Photo> queryList(int albumid);
}
