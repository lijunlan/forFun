package com.melonstudio.clientconnector;

import com.melonstudio.serverprotocal.DownLoadProtocol;
import com.melonstudio.serverprotocal.Protocol;
import com.melonstudio.serverprotocal.UpLoadProtocol;
import com.melonstudio.util.MyLog;

/**
 * 2014/07/31
 * 
 * @author guohaosu
 * 
 */
public class RequestManager {

	private static final String TAG = "com.melonstudio.clientconnector.RequestManager";

	/**
	 * 
	 * @param page
	 * @param formulate_time
	 * @return
	 */
	@Deprecated
	public String getMessageList(int page, boolean formulate_time) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_NEWS)
				.setMethod(DownLoadProtocol.METHOD_GET_MESSAGE_LIST)
				.setPage(page).setFormulateTime(formulate_time).build();
	}

	public String getMessageList(long refresh_time, boolean formulate_time) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_NEWS)
				.setMethod(DownLoadProtocol.METHOD_GET_MESSAGE_LIST)
				.setRefreshTime(refresh_time).setFormulateTime(formulate_time)
				.build();
	}

	/**
	 * 
	 * @param passageId
	 * @return
	 */
	public String deleteMessage(String passageId) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_NEWS)
				.setMethod(DownLoadProtocol.METHOD_DELETE_MESSAGE)
				.setPassageId(passageId).build();
	}

	/**
	 * 
	 * @param page
	 * @param id_type
	 * @param formulate_time
	 * @param user_id
	 * @param passage_id
	 * @return
	 */
	public String getCommentList(int page, String id_type,
			boolean formulate_time, String user_id, String passage_id) {
		RequestBuilder requestBuilder = new RequestBuilder(
				Protocol.MESSAGE_STYLE_COMMENT)
				.setMethod(DownLoadProtocol.METHOD_GET_COMMENT_LIST)
				.setPage(page).setIdType(id_type)
				.setFormulateTime(formulate_time);
		if (id_type.equals("user")) {
			requestBuilder.setUserId(user_id);
		} else if (id_type.equals("passage")) {
			requestBuilder.setPassageId(passage_id);
		}
		return requestBuilder.build();
	}

	public String deleteComment(String comment_id, String user_id,
			String passage_id) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_COMMENT)
				.setCommentId(comment_id).setUserId(user_id)
				.setPassageId(passage_id)
				.setMethod(DownLoadProtocol.METHOD_DELETE_COMMENT).build();
	}

	/**
	 * 
	 * @param passage_id
	 * @param user_id
	 * @param content
	 * @return
	 */
	public String uploadComment(String passage_id, String user_id,
			String content) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_COMMENT)
				.setPassageId(passage_id).setUserId(user_id)
				.setContent(content)
				.setMethod(UpLoadProtocol.METHOD_UPLOAD_COMMENT).build();
	}

	public String getUserInfo(String user_id) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_USER)
				.setUserId(user_id)
				.setMethod(DownLoadProtocol.METHOD_GET_USER_INFO).build();
	}

	public String login(String username, String password) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_USER)
				.setUsername(username).setMethod(DownLoadProtocol.METHOD_LOGIN)
				.setPassword(password).build();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public String logout(String id) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_USER)
				.setMethod(DownLoadProtocol.METHOD_LOGOUT).setId(id).build();
	}

	public String register(String username, String password, String email,
			String name) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_USER)
				.setUsername(username).setPassword(password).setEmail(email)
				.setName(name).setMethod(DownLoadProtocol.METHOD_REGISTER)
				.build();
	}

	public String changePassword(String user_id, String username,
			String old_password, String new_password) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_USER)
				.setUserId(user_id).setUsername(username)
				.setOldPassword(old_password).setNewPassword(new_password)
				.setMethod(DownLoadProtocol.METHOD_CHANGE_PASSWORD).build();
	}

	public String changeInfo(String user_id, int age, String name,
			String introduce, String iconUrl, String email, String phone,
			String sex, String isShowPhone, String isShowEmail) {
		RequestBuilder requestBuilder = new RequestBuilder(
				Protocol.MESSAGE_STYLE_USER)
				.setMethod(DownLoadProtocol.METHOD_CHANGE_INFO);
		if (user_id != null) {
			requestBuilder.setUserId(user_id);
		}
		if (age != 0) {
			requestBuilder.setAge(age);
		}
		if (name != null && !"".equals(name)) {
			requestBuilder.setName(name);
		}
		if (introduce != null && !"".equals(introduce)) {
			requestBuilder.setIntroduce(introduce);
		}
		if (iconUrl != null && !"".equals(iconUrl)) {
			requestBuilder.setIconUrl(iconUrl);
		}
		if (email != null && !"".equals(email)) {
			requestBuilder.setEmail(email);
		}
		if (phone != null && !"".equals(phone)) {
			requestBuilder.setPhone(phone);
		}
		if (sex != null && !"".equals(sex)) {
			requestBuilder.setSex(sex);
		}
		if (isShowEmail != null && !"".equals(isShowEmail)) {
			requestBuilder.setShowEmail(isShowEmail);
		}
		if (isShowPhone != null && !"".equals(isShowPhone)) {
			requestBuilder.setShowPhone(isShowPhone);
		}
		return requestBuilder.build();
	}

	public String checkPraise(String user_id, String type, String passage_id,
			String comment_id) {
		RequestBuilder requestBuilder = new RequestBuilder(
				Protocol.MESSAGE_STYLE_USER).setUserId(user_id).setType(type)
				.setMethod(DownLoadProtocol.METHOD_CHECK_PRAISE);
		if (passage_id != null && !"".equals(passage_id)) {
			log("check for passage praise");
			return requestBuilder.setPassageId(passage_id).build();
		} else if (comment_id != null && !"".equals(comment_id)) {
			log("check for comment praise");
			return requestBuilder.setCommentId(comment_id).build();
		} else {
			log("There is no passage_id and no comment_id");
			return null;
		}
	}

	public String praise(String user_id, String type, String passage_id,
			String comment_id) {
		RequestBuilder requestBuilder = new RequestBuilder(
				Protocol.MESSAGE_STYLE_USER).setUserId(user_id).setType(type)
				.setMethod(DownLoadProtocol.METHOD_PRAISE);
		if (passage_id != null && !"".equals(passage_id)) {
			log("praise for message");
			return requestBuilder.setPassageId(passage_id).build();
		} else if (comment_id != null && !"".equals(comment_id)) {
			log("praise for comment");
			return requestBuilder.setCommentId(comment_id).build();
		} else {
			log("There is no type you praise for");
			return null;
		}
	}

	public String cancel_parise(String user_id, String type, String passage_id,
			String comment_id) {
		RequestBuilder requestBuilder = new RequestBuilder(
				Protocol.MESSAGE_STYLE_USER).setUserId(user_id).setType(type)
				.setMethod(DownLoadProtocol.METHOD_CANCEL_PRAISE);
		if (passage_id != null && !"".equals(passage_id)) {
			log("praise for message");
			return requestBuilder.setPassageId(passage_id).build();
		} else if (comment_id != null && !"".equals(comment_id)) {
			log("praise for comment");
			return requestBuilder.setCommentId(comment_id).build();
		} else {
			log("There is no type you praise for");
			return null;
		}
	}

	public String get_msg_list(String own_user_id, String other_user_id,
			long refresh_time, boolean formulate_time) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_MESSAGE)
				.setMethod(DownLoadProtocol.METHOD_GET_MSG_LIST)
				.setOwn_User_Id(own_user_id).setOther_User_Id(other_user_id)
				.setRefreshTime(refresh_time).setFormulateTime(formulate_time)
				.build();
	}

	public String delete_msg(String message_id, String from_user_id,
			String to_user_id) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_MESSAGE)
				.setMethod(DownLoadProtocol.METHOD_DELETE_MSG)
				.setMessageId(message_id).setFrom_User_Id(from_user_id)
				.setTo_User_Id(to_user_id).build();
	}

	public String upload_msg(String from_user_id, String to_user_id,
			String content) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_MESSAGE)
				.setMethod(UpLoadProtocol.METHOD_UPLOAD_MSG)
				.setFrom_User_Id(from_user_id).setTo_User_Id(to_user_id)
				.setContent(content).build();
	}

	public String delete_many_msg(String own_user_id, String another_user_id) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_MESSAGE)
				.setMethod(DownLoadProtocol.METHOD_DELETE_MANY_MSG)
				.setOwn_User_Id(own_user_id)
				.setAnother_User_Id(another_user_id).build();
	}

	public String get_recent_list(String user_id, int page,
			boolean formulate_time) {
		return new RequestBuilder(Protocol.MESSAGE_STYLE_MESSAGE)
				.setMethod(DownLoadProtocol.METHOD_GET_RECENT_LIST)
				.setUserId(user_id).setPage(page)
				.setFormulateTime(formulate_time).build();
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}
}
