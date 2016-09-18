package com.melonstudio.serverprotocal;

public class UpLoadProtocol {

	/**
	 * style=news<br/>
	 * method
	 */
	public static final String METHOD_UPLOAD_MESSAGE = "upload_message";
	public static final String METHOD_UPLOAD_IMAGE = "upload_image";
	public static final String METHOD_UPLOAD_VIDEO = "upload_video";
	public static final String METHOD_UPLOAD_VOICE = "upload_voice";

	/**
	 * style=comment<br/>
	 * method
	 */
	public static final String METHOD_UPLOAD_COMMENT = "upload_comment";
	public static final String METHOD_UPLOAD_MSG = "upload_msg";

	/**
	 * type in sql
	 */
	public static final short SQL_TYPE_MESSAGE = 1;
	public static final short SQL_TYPE_IMAGE = 2;
	public static final short SQL_TYPE_VIDEO = 3;
	public static final short SQL_TYPE_VOICE = 4;
}
