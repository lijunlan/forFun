package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.ExUtil;
import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Data.SuperMap;
import com.sdll18.umedia.Persistant.Application;
import com.sdll18.umedia.Service.ApplicationService;

public class DownAppInfoService extends MsgService {

	private static final String STATIC_PATH = "http://www.sdll18.com/UMediaService/downfile?app_id=";

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
		int appid = Integer.parseInt(getData().get("app_id"));
		Application application = getApplicationService().query(appid, false);
		if (application != null) {
			SuperMap toSend = new SuperMap();
			toSend.put("id", application.getId())
					.put("name", application.getName())
					.put("url", application.getUrl())
					.put("key", application.getSecretKey())
					.put("description", application.getDescription())
					.put("time",
							ExUtil.time2String(application.getCreateTime(),
									true))
					.put("downAppUrl", STATIC_PATH + appid);
			setResMsg(toSend.finishByJson());
		} else {
			setResMsg(MsgUtil.getErrorMsg("application is not existed"));
		}
	}

}
