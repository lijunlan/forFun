package com.sdll18.server.network.handler;

import org.apache.mina.core.session.IoSession;

public class ExceptionHandler {

	public static void handle(IoSession session, Throwable cause){
		//TODO
		cause.printStackTrace();
	}
}
