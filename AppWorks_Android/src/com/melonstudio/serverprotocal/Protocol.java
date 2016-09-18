package com.melonstudio.serverprotocal;

public class Protocol {

	/**
	 * style
	 */
	public static final String MESSAGE_STYLE_USER = "user";
	public static final String MESSAGE_STYLE_COMMENT = "comment";
	public static final String MESSAGE_STYLE_NEWS = "news";
	public static final String MESSAGE_STYLE_NOTIFICATION = "notification";
	public static final String MESSAGE_STYLE_MESSAGE = "message";

	/**
	 * user state
	 */
	public static final short STATE_ONLINE = 1;
	public static final short STATE_OFFLINE = 0;

	public static final short VALID_ENABLE = 1;
	public static final short VALID_DISABLE = 0;

	public static final short SEX_MALE = 0;
	public static final short SEX_FEMALE = 1;

	/**
	 * 
	 */
	public static final int REGISTER = 1;
	public static final int LOGIN = 2;
	public static final int LOGOUT = 3;

}
