package com.ivypro.Service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.ivypro.Dao.UserDao;
import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Persistant.User;
import com.ivypro.Service.UserService;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void create(ExUser user) throws Exception {
		User u = getUserDao().query(user.getEmail(), false);
		if (u == null) {
			getUserDao().create((User) user.toPersistant());
		}else{
			throw new Exception("email has been registered");
		}
	}

	@Override
	public void remove(ExUser user) {
		getUserDao().remove((User) user.toPersistant());
	}

	@Override
	public void remove(int id) {
		getUserDao().remove(id);
	}

	@Override
	public void update(ExUser user) {
		getUserDao().update((User) user.toPersistant());
	}

	@Override
	public ExUser query(int id, boolean lazy) throws Exception {
		User user = getUserDao().query(id, lazy);
		if(user == null)throw new Exception("user is not existed");
		ExUser exUser = new ExUser();
		exUser.setUpByPersistant(user);
		return exUser;
	}

	@Override
	public ExUser query(String email, boolean lazy) throws Exception {
		User user = getUserDao().query(email, lazy);
		if(user == null)throw new Exception("user is not existed");
		ExUser exUser = new ExUser();
		exUser.setUpByPersistant(user);
		return exUser;
	}

	@Override
	public List<ExUser> queryList(int page, int pageSize, boolean lazy) {
		List<User> users = getUserDao().queryList(page, pageSize, lazy);
		List<ExUser> exUsers = new ArrayList<ExUser>();
		for (User user : users) {
			ExUser exUser = new ExUser();
			exUser.setUpByPersistant(user);
			exUsers.add(exUser);
		}
		return exUsers;
	}

	private static final int PAGESIZE_INT = 10;

	@Override
	public List<ExUser> queryList(int page, boolean lazy) {
		return queryList(page, PAGESIZE_INT, lazy);
	}

}
