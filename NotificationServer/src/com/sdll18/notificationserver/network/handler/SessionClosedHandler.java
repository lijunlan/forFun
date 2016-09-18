package com.sdll18.notificationserver.network.handler;

import org.apache.mina.core.session.IoSession;

import com.sdll18.notificationserver.network.SessionManager;
import com.sdll18.notificationserver.network.UserManager;

public class SessionClosedHandler {

	public static void handle(IoSession session) {
		SessionManager.getManager().removeSession(session);
		UserManager manager = UserManager.getManager();
		if (manager.containSession(session)) {
			manager.removeSession(session);
		}
		// TODO
	}
}
