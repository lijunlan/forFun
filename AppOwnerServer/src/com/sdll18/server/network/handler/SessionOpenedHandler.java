package com.sdll18.server.network.handler;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.network.data.SuperMap;

public class SessionOpenedHandler {

	public static void handle(IoSession session){
		session.write(new SuperMap("txt").put("msg", "welcome to server")
				.finishByJson());
		//TODO
	}
}
