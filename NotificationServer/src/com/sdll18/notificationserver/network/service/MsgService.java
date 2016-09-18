package com.sdll18.notificationserver.network.service;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

public abstract class MsgService {

	private String resMsg = "{'state':'unknown error}";
	private Map<String, String> inMap;
	private IoSession session;

	protected MsgService() {
	}

	public boolean setDataMap(Map<String, String> data) {
		inMap = data;
		return checkData();
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	protected IoSession getSession() {
		return session;
	}

	protected Map<String, String> getData() {
		return inMap;
	}

	protected abstract boolean checkData();

	public abstract void doit();

	protected void setResMsg(String msg) {
		resMsg = msg;
	}

	public String getResponseMsg() {
		return resMsg;
	}
}
