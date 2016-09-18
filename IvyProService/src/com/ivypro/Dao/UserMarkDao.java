package com.ivypro.Dao;

import com.ivypro.Persistant.User;
import com.ivypro.Persistant.UserMark;

public interface UserMarkDao {

	void create(UserMark userMark);

	void remove(UserMark userMark);

	void remove(String UUID);

	void update(UserMark userMark);

	void update(String userId, String UUID);

	UserMark queryUUID(int userId);

	UserMark query(String UUID);

	User queryUser(String UUID);
}
