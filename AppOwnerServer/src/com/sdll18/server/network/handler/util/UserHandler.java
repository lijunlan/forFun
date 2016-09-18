package com.sdll18.server.network.handler.util;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.data.MessageStyle;
import com.sdll18.server.network.SessionManager;

public class UserHandler extends MsgHandler {

	public UserHandler(IoSession session, Map<String, String> message) {
		super(session, message);
	}

	@Override
	protected void setUpMap() {
		getMap().put("style", MessageStyle.MESSAGE_STYLE_USER);

	}

	@Override
	public void doit() {
		setUpMap();
		String method = getMessage().get("method");
		// TODO ..
		if ("register".equals(method)) {
			// TODO ..
			getMap().put("method", "register_success");
		}
		if("login".equals(method)){
			//TODO ..
			SessionManager.getManager().addSession(12345, getSession());
			//TODO ..
		}
		if("logout".equals(method)){
			//TODO ..
			SessionManager.getManager().removeSessionByUserId(12345);
			//TODO ..
		}
		sendMsg();
	}

}
