package com.sdll18.springshoot.Service;

import java.util.List;

import com.sdll18.springshoot.Persistant.User;

public interface UserService {

	void create(User user);

	void remove(User user);

	void remove(int id);

	void update(User user);

	User query(int id);

	User query(String username);

	List<User> queryList(int page, int pageSize);
}
