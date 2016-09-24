package com.sdll18.weixin.Deal.Tool;

import java.util.HashMap;
import java.util.Map;

public abstract class MsgTool {

	protected String resMsg;
	protected Map<String, String> resMap;
	protected Map<String, String> mData;

	protected MsgTool(Map<String, String> data) {
		mData = data;
		setUpMap();
	}

	protected void setUpMap() {
		resMap = new HashMap<String, String>();
	}

	public abstract void doit();

	public String getResponseMsg() {
		return resMsg;
	}

}
