package com.sdll18.notificationserver.ui;

public class C {

	public C(String message) {

	}

	/**
	 * 控制台消息输出
	 * 
	 * @param tag
	 *            类标志
	 * @param message
	 *            消息
	 */
	public static void i(String tag, String message) {
		System.out.println("i>" + tag + ">" + message);
	}

	/**
	 * 控制台网络消息输出
	 * 
	 * @param session
	 *            会话标志
	 * @param message
	 *            消息
	 */
	public static void s(String session, String message) {
		System.out.println("#Network< remote ip:" + session + " >" + message);
	}

}
