package com.melonstudio.clientconnector;

import com.melonstudio.util.SuperMap;

/**
 * 2014/07/31
 * 
 * @author guohaosu
 * 
 */
public class RequestBuilder {
	private SuperMap map;

	public SuperMap getMap() {
		return map;
	}

	public void setMap(SuperMap map) {
		this.map = map;
	}

	public RequestBuilder() {
		map = new SuperMap();
	}

	public RequestBuilder(String style) {
		map = new SuperMap(style);
	}

	public String build() {
		return map.finishByJson();
	}

	public RequestBuilder setMethod(String method) {
		map.put("method", method);
		return this;
	}

	public RequestBuilder setPage(int page) {
		map.put("page", page);
		return this;
	}

	public RequestBuilder setRefreshTime(long refresh_time) {
		map.put("refresh_time", refresh_time);
		return this;
	}

	public RequestBuilder setFormulateTime(boolean formulate_time) {
		map.put("formulate_time", formulate_time);
		return this;
	}

	public RequestBuilder setPassageId(String passage_id) {
		map.put("passage_id", passage_id);
		return this;
	}

	public RequestBuilder setCommentId(String comment_id) {
		map.put("comment_id", comment_id);
		return this;
	}

	public RequestBuilder setIdType(String id_type) {
		map.put("id_type", id_type);
		return this;
	}

	public RequestBuilder setId(String id) {
		map.put("id", id);
		return this;
	}

	public RequestBuilder setUserId(String user_id) {
		map.put("user_id", user_id);
		return this;
	}

	public RequestBuilder setContent(String content) {
		map.put("content", content);
		return this;
	}

	public RequestBuilder setUsername(String username) {
		map.put("username", username);
		return this;
	}

	public RequestBuilder setPassword(String password) {
		map.put("password", password);
		return this;
	}

	public RequestBuilder setEmail(String email) {
		map.put("email", email);
		return this;
	}

	/**
	 * set the nick name
	 * 
	 * @param name
	 *            nick name
	 * @return the Builder Object
	 */
	public RequestBuilder setName(String name) {
		map.put("name", name);
		return this;
	}

	public RequestBuilder setOldPassword(String old_password) {
		map.put("old_password", old_password);
		return this;
	}

	public RequestBuilder setNewPassword(String new_password) {
		map.put("new_password", new_password);
		return this;
	}

	public RequestBuilder setAge(int age) {
		map.put("age", age);
		return this;
	}

	public RequestBuilder setIntroduce(String introduce) {
		map.put("introduce", introduce);
		return this;
	}

	public RequestBuilder setIconUrl(String iconUrl) {
		map.put("iconUrl", iconUrl);
		return this;
	}

	public RequestBuilder setPhone(String phone) {
		map.put("phone", phone);
		return this;
	}

	public RequestBuilder setSex(String sex) {
		map.put("sex", sex);
		return this;
	}

	public RequestBuilder setType(String type) {
		map.put("type", type);
		return this;
	}

	public RequestBuilder setMessageId(String message_id) {
		map.put("message_id", message_id);
		return this;
	}

	public RequestBuilder setFrom_User_Id(String from_user_id) {
		map.put("from_user_id", from_user_id);
		return this;
	}

	public RequestBuilder setTo_User_Id(String to_user_id) {
		map.put("to_user_id", to_user_id);
		return this;
	}

	public RequestBuilder setOwn_User_Id(String own_user_id) {
		map.put("own_user_id", own_user_id);
		return this;
	}

	public RequestBuilder setOther_User_Id(String other_user_id) {
		map.put("other_user_id", other_user_id);
		return this;
	}

	public RequestBuilder setAnother_User_Id(String another_user_id) {
		map.put("another_user_id", another_user_id);
		return this;
	}

	public RequestBuilder setShowPhone(String isShowPhone) {
		map.put("isShowPhone", isShowPhone);
		return this;
	}

	public RequestBuilder setShowEmail(String isShowEmail) {
		map.put("isShowEmail", isShowEmail);
		return this;
	}

}
