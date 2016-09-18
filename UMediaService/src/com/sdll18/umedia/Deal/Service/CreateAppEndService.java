package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Persistant.Application;
import com.sdll18.umedia.Service.ApplicationService;

public class CreateAppEndService extends MsgService {

	private ApplicationService applicationService;

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("app_id");
	}

	@Override
	public void doit() {
		int id = Integer.parseInt(getData().get("app_id"));
		Application application = getApplicationService().query(id, false);
		application.setState("created");
		getApplicationService().update(application);
		setResMsg(MsgUtil.getSuccessMsg("end successfully"));
	}

}
