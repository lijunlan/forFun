package com.ivypro.Service;

import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Persistant.UserMark;

public interface UserMarkService {

	void create(UserMark userMark);
	
	void create(String userId,String UUID);

	void remove(UserMark userMark);

	void remove(String UUID);

	UserMark query(String UUID);

	ExUser queryUser(String UUID) throws Exception;
}
