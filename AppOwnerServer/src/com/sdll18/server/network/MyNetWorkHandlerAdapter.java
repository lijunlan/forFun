package com.sdll18.server.network;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.sdll18.server.network.handler.ExceptionHandler;
import com.sdll18.server.network.handler.MsgReceiveHandler;
import com.sdll18.server.network.handler.SessionClosedHandler;
import com.sdll18.server.network.handler.SessionOpenedHandler;
import com.sdll18.server.ui.C;

public class MyNetWorkHandlerAdapter implements IoHandler {

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		C.s(session.getRemoteAddress().toString(), "Error:" + cause.getMessage());
		ExceptionHandler.handle(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		C.s(session.getRemoteAddress().toString(), "Recieve:" + message.toString());
		MsgReceiveHandler.handle(session, message);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		C.s(session.getRemoteAddress().toString(), "Sent:" + message.toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		C.s(session.getRemoteAddress().toString(), "Closed");
		SessionClosedHandler.handle(session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// DO NOTHING
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// DO NOTHING	
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		C.s(session.getRemoteAddress().toString(), "连接建立成功");
		SessionOpenedHandler.handle(session);
	}

}
