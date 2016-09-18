package com.ivypro.Service;

import java.util.List;

import com.ivypro.ExchangeData.ExUser;



public interface UserService {

	void create(ExUser user) throws Exception;

	void remove(ExUser user);

	void remove(int id);

	void update(ExUser user);

	ExUser query(int id, boolean lazy) throws Exception;

	ExUser query(String email, boolean lazy) throws Exception;

	List<ExUser> queryList(int page, int pageSize, boolean lazy);

	List<ExUser> queryList(int page, boolean lazy);

}
