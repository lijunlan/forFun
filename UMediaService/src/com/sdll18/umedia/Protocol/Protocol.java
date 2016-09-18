package com.sdll18.umedia.Protocol;

public class Protocol {

	/**
	 * style
	 */
	public static final String STYLE_USER = "user";
	public static final String STYLE_APPLICATION = "application";

	/**
	 * method style=user
	 */
	public static final String METHOD_LOGIN = "login";
	public static final String METHOD_REGISTER = "register";
	public static final String METHOD_CHANGE_USER_INFO = "change_info";
	public static final String METHOD_CHANGE_USER_PASSWORD = "change_password";
	public static final String METHOD_GET_USER_INFO = "get_user_info";

	/**
	 * method style=application
	 */
	public static final String METHOD_APPLICATION_CREATE = "create";
	public static final String METHOD_APPLICATION_UPDATE = "update";
	public static final String METHOD_APPLICATION_DELETE = "delete";
	public static final String METHOD_APPLICATION_GET_INFO = "get_app_info";
	public static final String METHOD_APPLICATION_GET_LIST = "get_app_list";
	public static final String METHOD_APPLICATION_END = "create_end";

	/**
	 * user state
	 */
	public static final short STATE_ONLINE = 1;
	public static final short STATE_OFFLINE = 0;

	public static final short VALID_ENABLE = 1;
	public static final short VALID_DISABLE = 0;

	public static final short SEX_MALE = 0;
	public static final short SEX_FEMALE = 1;
}
