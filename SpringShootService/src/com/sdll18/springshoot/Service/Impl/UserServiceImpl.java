package com.sdll18.springshoot.Service.Impl;

import java.util.List;

import com.sdll18.springshoot.Dao.UserDao;
import com.sdll18.springshoot.Persistant.User;
import com.sdll18.springshoot.Service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void create(User user) {
		getUserDao().create(user);
	}

	@Override
	public void remove(User user) {
		getUserDao().remove(user);
	}

	@Override
	public void remove(int id) {
		getUserDao().remove(id);
	}

	@Override
	public void update(User user) {
		getUserDao().update(user);
	}

	@Override
	public User query(int id) {
		return getUserDao().query(id);
	}

	@Override
	public User query(String username) {
		return getUserDao().query(username);
	}

	@Override
	public List<User> queryList(int page, int pageSize) {
		return getUserDao().queryList(page, pageSize);
	}

}
