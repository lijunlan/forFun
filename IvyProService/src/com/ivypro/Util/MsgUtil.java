package com.ivypro.Util;

public class MsgUtil {

	public static String getErrorMsg(String msg) {
		return "{'state':'error','msg':'" + msg + "'}";
	}

	public static String getSuccessMsg(String msg) {
		return "{'state':'success','msg':'" + msg + "'}";
	}
}
