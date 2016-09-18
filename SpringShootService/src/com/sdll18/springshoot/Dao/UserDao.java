package com.sdll18.springshoot.Dao;

import java.util.List;

import com.sdll18.springshoot.Persistant.User;

public interface UserDao {

	void create(User user);

	void remove(User user);

	void remove(int id);

	void update(User user);

	void updateFromSql(String sql);

	User query(int id);

	User query(String username);

	List<User> queryList(int page, int pageSize);
}