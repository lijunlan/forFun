package com.sdll18.server.network;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

public class SessionManager {

	public static SessionManager singleInstance;

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
	/**
	 * String1:userId<br>
	 * String2:sessionId
	 */
	private Map<String, String> mRecord;

	/**
	 * String1:sessionId<br>
	 * String2:userId
	 */
	private Map<String, String> mRecord2;

	private SessionManager() {
		mSessionMap = new HashMap<String, IoSession>();
		mRecord = new HashMap<String, String>();
		mRecord2 = new HashMap<String, String>();
	}

	/**
	 * get session by userid
	 * 
	 * @param id
	 * @return
	 */
	public IoSession getSessionByUserId(String id) {
		return mSessionMap.get(mRecord.get(id));
	}

	/**
	 * get session by userid
	 * 
	 * @param id
	 * @return
	 */
	public IoSession getSessionByUserId(int id) {
		return mSessionMap.get(mRecord.get(String.valueOf(id)));
	}

	/**
	 * save session indexed by userid
	 * 
	 * @param id
	 * @param session
	 */
	public void addSession(String id, IoSession session) {
		mRecord.put(id, String.valueOf(session.getId()));
		mRecord2.put(String.valueOf(session.getId()), id);
		mSessionMap.put(String.valueOf(session.getId()), session);
	}

	/**
	 * save session indexed by userid
	 * 
	 * @param id
	 * @param session
	 */
	public void addSession(long id, IoSession session) {
		mRecord.put(String.valueOf(id), String.valueOf(session.getId()));
		mRecord2.put(String.valueOf(session.getId()), String.valueOf(id));
		mSessionMap.put(String.valueOf(session.getId()), session);
	}

	public void removeSessionBySessionId(String id) {
		String userId = mRecord2.get(id);
		mRecord.remove(userId);
		mSessionMap.remove(userId);
		mRecord2.remove(id);
	}
	
	public void removeSessionBySessionId(long id){
		removeSessionBySessionId(String.valueOf(id));
	}

	public void removeSessionByUserId(String id) {
		mRecord2.remove(mRecord.get(id));
		mRecord.remove(id);
		mSessionMap.remove(id);
	}

	public void removeSessionByUserId(long id) {
		removeSessionByUserId(String.valueOf(id));
	}
}
