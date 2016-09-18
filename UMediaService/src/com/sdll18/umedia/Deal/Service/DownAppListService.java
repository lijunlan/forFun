package com.sdll18.umedia.Deal.Service;

import java.util.ArrayList;
import java.util.List;
import com.sdll18.umedia.Data.ExUtil;
import com.sdll18.umedia.Data.Json;
import com.sdll18.umedia.Data.SuperMap;
import com.sdll18.umedia.Persistant.Application;
import com.sdll18.umedia.Service.ApplicationService;

public class DownAppListService extends MsgService {

	private ApplicationService applicationService;

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("user_id");
	}

	@Override
	public void doit() {
		int userid = Integer.parseInt(getData().get("user_id"));
		List<Application> applications = getApplicationService().queryList(
				userid);
		List<String> toSend = new ArrayList<String>();
		for (Application a : applications) {
			SuperMap temp = new SuperMap();
			temp.put("url", a.getUrl());
			temp.put("name", a.getName());
			temp.put("id", a.getId());
			temp.put("time", ExUtil.time2String(a.getCreateTime(), true));
			temp.put("state", a.getState());
			toSend.add(temp.finishByJson());
		}
		setResMsg(Json.getJson(toSend));

	}
}
