package com.sdll18.springshoot.Dao;

import java.util.List;

import com.sdll18.springshoot.Persistant.Album;

public interface AlbumDao {

	void create(Album album);

	void remove(Album album);

	void remove(int id);

	void update(Album album);

	void updateFromSql(String sql);

	Album query(int id, boolean lazy);

	List<Album> queryList(String category);
	
	List<Album> queryList(String category, int page, int pageSize, boolean lazy);

	List<Album> queryList(int page, int pageSize, boolean lazy);

}
