package com.sdll18.notificationserver.network;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

public class UserManager {

	private static UserManager singleInstance;

	public static synchronized UserManager getManager() {
		if (singleInstance == null)
			singleInstance = new UserManager();
		return singleInstance;
	}

	private Map<String, String> sessionIdMap;
	private Map<String, String> userIdMap;

	private UserManager() {
		sessionIdMap = new HashMap<String, String>();
		userIdMap = new HashMap<String, String>();
	}

	public void addUser(String user_id, IoSession session) {
		sessionIdMap.put(user_id, String.valueOf(session.getId()));
		userIdMap.put(String.valueOf(session.getId()), user_id);
	}

	public boolean containSession(IoSession session) {
		return sessionIdMap.containsValue(session);
	}

	public String getSessionId(String user_id) {
		return sessionIdMap.get(user_id);
	}

	public void removeSession(IoSession session) {
		String userid = userIdMap.get("" + session.getId());
		sessionIdMap.remove(userid);
		userIdMap.remove("" + session.getId());
	}
}
