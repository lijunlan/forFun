package com.sdll18.notificationserver.network.service;

import java.util.Iterator;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.sdll18.notificationserver.network.SessionManager;

public class SendNotificationService extends MsgService {

	@Override
	protected boolean checkData() {
		return getData().containsKey("content");
	}

	@Override
	public void doit() {
		Map<String, IoSession> sessionMap = SessionManager.getManager()
				.getSessions();
		Iterator<String> it = sessionMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			IoSession session = sessionMap.get(key);
			session.write(getData().get("content"));
		}
		setResMsg("{'state':'success','msg':'transmited successfully'}");
	}

}
