package com.ivypro.Dao;

import java.util.List;

import com.ivypro.Persistant.User;


public interface UserDao {
	
	void create(User user);

	void remove(User user);

	void remove(int id);

	void update(User user);

	void updateFromSql(String sql);

	User query(int id, boolean lazy);

	User query(String email, boolean lazy);

	List<User> queryList(int page, int pageSize, boolean lazy);
	
}
