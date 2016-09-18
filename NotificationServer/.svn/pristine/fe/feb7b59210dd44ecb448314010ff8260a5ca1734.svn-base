package com.sdll18.notificationserver.network.service;

import org.apache.mina.core.session.IoSession;

import com.sdll18.notificationserver.network.SessionManager;
import com.sdll18.notificationserver.network.UserManager;

public class SendMessageService extends MsgService {

	@Override
	protected boolean checkData() {
		return getData().containsKey("user_id")
				&& getData().containsKey("content");
	}

	@Override
	public void doit() {
		try {
			String user_id = getData().get("user_id");
			String content = getData().get("content");
			IoSession session = SessionManager.getManager()
					.getSessionBySessionId(
							UserManager.getManager().getSessionId(user_id));
			session.write(content);
			setResMsg("{'state':'success','msg':'transmited successfully'}");
		} catch (Exception e) {
			setResMsg("user is not online");
		}
	}

}
