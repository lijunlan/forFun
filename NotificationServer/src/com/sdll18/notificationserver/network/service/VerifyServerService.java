package com.sdll18.notificationserver.network.service;

import com.sdll18.notificationserver.network.SessionManager;
import com.sdll18.notificationserver.network.data.SuperMap;

public class VerifyServerService extends MsgService {

	@Override
	protected boolean checkData() {
		return true;
	}

	@Override
	public void doit() {
		SessionManager.getManager().removeSession(getSession());
		setResMsg(new SuperMap().put("state", "welcome to server")
				.finishByJson());
	}

}
