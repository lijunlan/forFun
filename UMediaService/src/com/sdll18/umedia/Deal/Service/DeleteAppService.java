package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Service.ApplicationService;

public class DeleteAppService extends MsgService {

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
		getApplicationService().remove(id);
		setResMsg(MsgUtil.getSuccessMsg("App has been removed"));
	}

}
