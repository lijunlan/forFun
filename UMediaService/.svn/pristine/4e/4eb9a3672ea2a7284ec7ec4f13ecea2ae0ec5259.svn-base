package com.sdll18.umedia.Service.Impl;

import java.util.List;

import com.sdll18.umedia.Dao.ApplicationDao;
import com.sdll18.umedia.Persistant.Application;
import com.sdll18.umedia.Service.ApplicationService;

public class ApplicationServiceImpl implements ApplicationService {

	private static final int PAGE_SIZE = 5;

	private ApplicationDao applicationDao;

	public ApplicationDao getApplicationDao() {
		return applicationDao;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	@Override
	public void create(Application application) {
		getApplicationDao().create(application);
	}

	@Override
	public void remove(Application application) {
		getApplicationDao().remove(application);
	}

	@Override
	public void remove(int id) {
		getApplicationDao().remove(id);
	}

	@Override
	public void update(Application application) {
		getApplicationDao().update(application);
	}

	@Override
	public Application query(int id, boolean lazy) {
		return getApplicationDao().query(id, lazy);
	}

	@Override
	public List<Application> queryList(int page, boolean lazy) {
		return getApplicationDao().queryList(page, PAGE_SIZE, lazy);
	}

	@Override
	public List<Application> queryList(int page, int pageSize, boolean lazy) {
		return getApplicationDao().queryList(page, pageSize, lazy);
	}

	@Override
	public List<Application> queryList(int userid) {
		return getApplicationDao().queryList(userid);
	}

}
