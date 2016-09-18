package com.sdll18.umedia.Dao;

import java.util.List;

import com.sdll18.umedia.Persistant.Application;

public interface ApplicationDao {

	void create(Application application);

	void remove(Application application);

	void remove(int id);

	void update(Application application);

	void updateFromSql(String sql);

	Application query(int id, boolean lazy);

	List<Application> queryList(int userid);

	List<Application> queryList(int page, int pageSize, boolean lazy);

}
