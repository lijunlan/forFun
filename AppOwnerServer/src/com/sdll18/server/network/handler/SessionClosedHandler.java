package com.sdll18.server.network.handler;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.network.SessionManager;

public class SessionClosedHandler {

	public static void handle(IoSession session){
		SessionManager.getManager().removeSessionBySessionId(session.getId());
		//TODO
	}
}
