package com.ivypro.Handle.Service;

import java.util.Map;

import com.ivypro.Service.UserMarkService;

public abstract class MsgService {

	private String resMsg = "{'state':'unknown error}";
	private Map<String, String> inMap;
	private UserMarkService userMarkService;

	protected Map<String, String> getInMap() {
		return inMap;
	}

	protected void setInMap(Map<String, String> inMap) {
		this.inMap = inMap;
	}

	public UserMarkService getUserMarkService() {
		return userMarkService;
	}

	public void setUserMarkService(UserMarkService userMarkService) {
		this.userMarkService = userMarkService;
	}

	protected MsgService() {
	}

	protected String getResMsg() {
		return resMsg;
	}

	public boolean setDataMap(Map<String, String> data) {
		inMap = data;
		return checkData();
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
