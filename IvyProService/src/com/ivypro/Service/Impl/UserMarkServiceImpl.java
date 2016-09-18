package com.ivypro.Service.Impl;

import com.ivypro.Dao.UserDao;
import com.ivypro.Dao.UserMarkDao;
import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Persistant.User;
import com.ivypro.Persistant.UserMark;
import com.ivypro.Service.UserMarkService;

public class UserMarkServiceImpl implements UserMarkService {

	private UserMarkDao userMarkDao;

	private UserDao userDao;

	public UserMarkDao getUserMarkDao() {
		return userMarkDao;
	}

	public void setUserMarkDao(UserMarkDao userMarkDao) {
		this.userMarkDao = userMarkDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void create(UserMark userMark) {
		getUserMarkDao().create(userMark);
	}

	@Override
	public void create(String userId, String UUID) {
		User user = getUserDao().query(Integer.valueOf(userId), false);
		UserMark um = getUserMarkDao().queryUUID(Integer.valueOf(userId));
		if (um == null) {
			UserMark userMark = new UserMark();
			userMark.setUser(user);
			userMark.setUuid(UUID);
			create(userMark);
		} else {
			getUserMarkDao().update(userId, UUID);
		}
	}

	@Override
	public void remove(UserMark userMark) {
		getUserMarkDao().remove(userMark);

	}

	@Override
	public void remove(String UUID) {
		getUserMarkDao().remove(UUID);
	}

	@Override
	public UserMark query(String UUID) {
		return getUserMarkDao().query(UUID);
	}

	@Override
	public ExUser queryUser(String UUID) throws Exception {
		User user = getUserMarkDao().queryUser(UUID);
		if (user == null)
			throw new Exception("mark is not existed");
		ExUser exUser = new ExUser();
		exUser.setUpByPersistant(user);
		return exUser;
	}

}
