package com.sdll18.server.data.Dao;

import java.util.List;

import com.sdll18.server.data.persistence.Passage;



public interface PassageDao {

	void create(Passage passage);

	void remove(Passage passage);
	
	void remove(int id);

	void update(Passage passage);
	
	void updateFromSql(String sql);

	Passage query(int id);

	List<Passage> queryList(int page,int pageSize);
}
