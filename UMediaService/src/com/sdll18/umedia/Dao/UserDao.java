package com.sdll18.umedia.Dao;

import java.util.List;

import com.sdll18.umedia.Persistant.User;


public interface UserDao {
	
	void create(User user);

	void remove(User user);

	void remove(int id);

	void update(User user);

	void updateFromSql(String sql);

	User query(int id, boolean lazy);

	User query(String username, boolean lazy);

	List<User> queryList(int page, int pageSize, boolean lazy);

	List<User> queryListByOnline(int page, int pageSize, boolean online,
			boolean lazy);

	List<User> queryListByValid(int page, int pageSize, boolean enable,
			boolean lazy);
}
