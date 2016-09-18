package com.sdll18.umedia.Service;

import java.util.List;

import com.sdll18.umedia.Persistant.Application;

public interface ApplicationService {

	void create(Application application);

	void remove(Application application);

	void remove(int id);

	void update(Application application);

	Application query(int id, boolean lazy);

	List<Application> queryList(int userid);

	List<Application> queryList(int page, boolean lazy);

	List<Application> queryList(int page, int pageSize, boolean lazy);
}
