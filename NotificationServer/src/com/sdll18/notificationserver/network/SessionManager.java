package com.sdll18.notificationserver.network;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

public class SessionManager {

	private static SessionManager singleInstance;

	public static synchronized SessionManager getManager() {
		if (singleInstance == null)
			singleInstance = new SessionManager();
		return singleInstance;
	}

	/**
	 * String:sessionId<br>
	 * IoSession:instance of session
	 */
	private Map<String, IoSession> mSessionMap;

	private SessionManager() {
		mSessionMap = new HashMap<String, IoSession>();
	}

	/**
	 * get session by sessionId
	 * 
	 * @param id
	 * @return
	 */
	public IoSession getSessionBySessionId(String id) {
		return mSessionMap.get(String.valueOf(id));
	}

	public Map<String, IoSession> getSessions() {
		return mSessionMap;
	}

	/**
	 * save session indexed by sessionid
	 * 
	 * @param id
	 * @param session
	 */
	public void addSession(IoSession session) {
		mSessionMap.put(String.valueOf(session.getId()), session);
	}

	public void removeSessionBySessionId(String id) {
		mSessionMap.remove(id);
	}

	public void removeSession(IoSession session) {
		mSessionMap.remove(session.getId());
	}

}
