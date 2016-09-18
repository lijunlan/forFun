package com.melonstudio.serverprotocal;

public class DownLoadProtocol {

	/**
	 * style = news<br/>
	 * method
	 */
	public static final String METHOD_GET_MESSAGE_LIST = "get_message_list";
	public static final String METHOD_DELETE_MESSAGE = "delete_message";

	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_MESSAGE = "message";
	public static final String TYPE_VIDEO = "video";

	/**
	 * style=comment<br/>
	 * method
	 */
	public static final String METHOD_GET_COMMENT_LIST = "get_comment_list";
	public static final String METHOD_DELETE_COMMENT = "delete_comment";

	/**
	 * style = user<br/>
	 * method
	 */
	public static final String METHOD_GET_USER_INFO = "get_user_info";
	public static final String METHOD_GET_USER_LIST = "get_user_list";
	public static final String METHOD_LOGIN = "login";
	public static final String METHOD_LOGOUT = "logout";
	public static final String METHOD_REGISTER = "register";
	public static final String METHOD_CHANGE_PASSWORD = "change_password";
	public static final String METHOD_CHANGE_INFO = "change_info";
	public static final String METHOD_DELETE_USER = "delete_user";
	public static final String METHOD_ENABLE_USER = "enable_user";
	public static final String METHOD_DISABLE_USER = "disable_user";
	public static final String METHOD_CHECK_PRAISE = "check_praise";
	public static final String METHOD_PRAISE = "praise";
	public static final String METHOD_CANCEL_PRAISE = "cancel_praise";

	/**
	 * style = message
	 */
	public static final String METHOD_GET_MSG_LIST = "get_msg_list";
	public static final String METHOD_DELETE_MSG = "delete_msg";
	public static final String METHOD_DELETE_MANY_MSG = "delete_many_msg";
	public static final String METHOD_GET_RECENT_LIST = "get_recent_list";

}
