package com.sdll18.springshoot.Service;

import java.util.List;

import com.sdll18.springshoot.Persistant.Album;

public interface AlbumService {

	void create(Album album);

	void remove(Album album);

	void remove(int id);

	void update(Album album);

	Album query(int id, boolean lazy);

	List<Album> queryList(String category);
	
	List<Album> queryList(String category, int page, boolean lazy);

	List<Album> queryList(String category, int page, int pageSize, boolean lazy);

	List<Album> queryList(int page, boolean lazy);

	List<Album> queryList(int page, int pageSize, boolean lazy);
}
