package com.sdll18.server.data.Dao;

import java.util.List;

import com.sdll18.server.data.persistence.Passage;
import com.sdll18.server.data.persistence.User;

public interface UserDao {
	
	void create(User user);

	void remove(User user);
	
	void remove(int id);

	void update(User user);
	
	void updateFromSql(String sql);

	User query(int id);
	
	User query(String username);

	List<Passage> queryList(int page,int pageSize);
}
