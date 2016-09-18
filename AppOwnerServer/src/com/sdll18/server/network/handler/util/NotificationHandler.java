package com.sdll18.server.network.handler.util;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.data.MessageStyle;
import com.sdll18.server.network.SessionManager;

public class NotificationHandler extends MsgHandler {

	public NotificationHandler(IoSession session, Map<String, String> message) {
		super(session, message);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUpMap() {
		getMap().put("style", MessageStyle.MESSAGE_STYLE_NOTIFICATION);
	}

	@Override
	public void doit() {
		setUpMap();
		String method = getMessage().get("method");
		// TODO
		if ("news_available".equals(method)) {
			getMap().put("method", "news_available");
		}
		sendMsg();
	}

	@Override
	protected void sendMsg() {
		// TODO
		SessionManager.getManager().getSessionByUserId("12345")
				.write(getMap().finishByJson());
		// TODO
	}

}
