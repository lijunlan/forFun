package com.sdll18.umedia.Service.Impl;

import java.util.List;

import com.sdll18.umedia.Dao.UserDao;
import com.sdll18.umedia.Persistant.User;
import com.sdll18.umedia.Service.UserService;

public class UserServiceImpl implements UserService {

	private static final int PAGE_SIZE = 20;

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
	public User query(int id, boolean lazy) {
		return getUserDao().query(id, lazy);
	}

	@Override
	public User query(String username, boolean lazy) {
		return getUserDao().query(username, lazy);
	}

	@Override
	public List<User> queryList(int page, int pageSize, boolean lazy) {
		return getUserDao().queryList(page, pageSize, lazy);
	}

	@Override
	public List<User> queryList(int page, boolean lazy) {
		return getUserDao().queryList(page, PAGE_SIZE, lazy);
	}

	@Override
	public List<User> queryListByOnline(int page, int pageSize, boolean online,
			boolean lazy) {
		return getUserDao().queryListByOnline(page, pageSize, online, lazy);
	}

	@Override
	public List<User> queryListByOnline(int page, boolean online, boolean lazy) {
		return getUserDao().queryListByOnline(page, PAGE_SIZE, online, lazy);
	}

	@Override
	public List<User> queryListByValid(int page, boolean enable, boolean lazy) {
		return getUserDao().queryListByValid(page, PAGE_SIZE, enable, lazy);
	}

	@Override
	public List<User> queryListByValid(int page, int pageSize, boolean enable,
			boolean lazy) {
		return getUserDao().queryListByValid(page, pageSize, enable, lazy);
	}
}
