package com.melonstudio.clientconnector;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.melonstudio.util.MyLog;

/**
 * 
 * @author Wo
 * 
 */
public class ContentServerHandler extends IoHandlerAdapter {

	private final static String TAG = "com.melonstudio.clientconnector.ContentServerHandler";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private MinaContentManager minaContentManager;

	public ContentServerHandler(MinaContentManager manager) {
		this.minaContentManager = manager;
	}

	/**
	 * process the message I received. And then convert the json string to the
	 * Passage Object. And then, based on the style attribute, process the
	 * passage object
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String string = message.toString();
		JSONObject jsonObject = new JSONObject(string);
		log("Message Received: " + jsonObject.toString());
		minaContentManager.handleMessage(jsonObject);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		String msg = (String) message;
		log("Message sent: " + msg);
		super.messageSent(session, message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		log("session Closed");
		super.sessionClosed(session);
		minaContentManager.connectionLost();
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		log("session Created");
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("method", MinaContentManager.START);
			String user_id = minaContentManager.getUser_id();
			if (user_id == null || "".equals(user_id)) {
				user_id = "null";
			}
			jsonObject.put("user_id", user_id);
			jsonObject.put("refresh_time", System.currentTimeMillis());
			jsonObject.put("style", "mina");
			jsonObject.put("formulate_time", String.valueOf(false));
			log("send json string: " + jsonObject.toString());
			session.write(jsonObject.toString());
		} catch (Exception e) {
			log("Exception in sessionCreated: " + e.toString());
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		log("Session Idle");
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		log("session Opened");
		minaContentManager.connected();
	}
}
