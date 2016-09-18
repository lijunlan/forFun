package com.melonstudio.clientconnector;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentParameter implements Serializable {
	private static final long serialVersionUID = -1856210685673144790L;
	private int page;
	private boolean formulate_time;
	private String passage_id;
	private String id_type;
	private String user_id;
	private String comment_id;
	private String content;
	private String username;
	private String password;
	private String old_password;
	private String new_password;
	private long refresh_time;
	private int age;
	private String name;
	private String introduce;
	private String iconUrl;
	private String email;
	private String phone;
	private String sex;
	private String type;
	private String own_user_id;
	private String other_user_id;
	private String another_user_id;
	private String from_user_id;
	private String to_user_id;
	private String message_id;

	private String showPhone;
	private String showEmail;

	public String getShowPhone() {
		return showPhone;
	}

	public void setShowPhone(String showPhone) {
		this.showPhone = showPhone;
	}

	public String getShowEmail() {
		return showEmail;
	}

	public void setShowEmail(String showEmail) {
		this.showEmail = showEmail;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public long getRefresh_time() {
		return refresh_time;
	}

	public void setRefresh_time(long refresh_time) {
		this.refresh_time = refresh_time;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public boolean isFormulate_time() {
		return formulate_time;
	}

	public void setFormulate_time(boolean formulate_time) {
		this.formulate_time = formulate_time;
	}

	public String getPassage_id() {
		return passage_id;
	}

	public void setPassage_id(String passage_id) {
		this.passage_id = passage_id;
	}

	public String getId_type() {
		return id_type;
	}

	public void setId_type(String id_type) {
		this.id_type = id_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOld_password() {
		return old_password;
	}

	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwn_user_id() {
		return own_user_id;
	}

	public void setOwn_user_id(String own_user_id) {
		this.own_user_id = own_user_id;
	}

	public String getOther_user_id() {
		return other_user_id;
	}

	public void setOther_user_id(String other_user_id) {
		this.other_user_id = other_user_id;
	}

	public String getFrom_user_id() {
		return from_user_id;
	}

	public void setFrom_user_id(String from_user_id) {
		this.from_user_id = from_user_id;
	}

	public String getTo_user_id() {
		return to_user_id;
	}

	public void setTo_user_id(String to_user_id) {
		this.to_user_id = to_user_id;
	}

	public String getAnother_user_id() {
		return another_user_id;
	}

	public void setAnother_user_id(String another_user_id) {
		this.another_user_id = another_user_id;
	}

	public static JSONObject createJsonObject() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("page", 0);
			jsonObject.put("formulate_time", false);
			jsonObject.put("passage_id", "");
			jsonObject.put("id_type", "");
			jsonObject.put("user_id", "");
			jsonObject.put("comment_id", "");
			jsonObject.put("content", "");
			jsonObject.put("username", "");
			jsonObject.put("old_password", "");
			jsonObject.put("new_password", "");
			jsonObject.put("refresh_time", System.currentTimeMillis());
			jsonObject.put("password", "");
			jsonObject.put("age", 0);
			jsonObject.put("name", "");
			jsonObject.put("introduce", "");
			jsonObject.put("iconUrl", "");
			jsonObject.put("email", "");
			jsonObject.put("phone", "");
			jsonObject.put("sex", "");
			jsonObject.put("type", "");
			jsonObject.put("own_user_id", "");
			jsonObject.put("other_user_id", "");
			jsonObject.put("another_user_id", "");
			jsonObject.put("from_user_id", "");
			jsonObject.put("to_user_id", "");
			jsonObject.put("message_id", "");
			jsonObject.put("showEmail", "");
			jsonObject.put("showPhone", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static ContentParameter createFromJSONObject(JSONObject jsonObject) {
		ContentParameter parameter = new ContentParameter();
		try {
			parameter.setPage(jsonObject.getInt("page"));
			parameter
					.setFormulate_time(jsonObject.getBoolean("formulate_time"));
			parameter.setPassage_id(jsonObject.getString("passage_id"));
			parameter.setId_type(jsonObject.getString("id_type"));
			parameter.setMessage_id(jsonObject.getString("message_id"));
			parameter.setUser_id(jsonObject.getString("user_id"));
			parameter.setComment_id(jsonObject.getString("comment_id"));
			parameter.setContent(jsonObject.getString("content"));
			parameter.setUsername(jsonObject.getString("username"));
			parameter.setOld_password(jsonObject.getString("old_password"));
			parameter.setNew_password(jsonObject.getString("new_password"));
			parameter.setRefresh_time(jsonObject.getLong("refresh_time"));
			parameter.setPassword(jsonObject.getString("password"));
			parameter.setAge(jsonObject.getInt("age"));
			parameter.setName(jsonObject.getString("name"));
			parameter.setIntroduce(jsonObject.getString("introduce"));
			parameter.setIconUrl(jsonObject.getString("iconUrl"));
			parameter.setEmail(jsonObject.getString("email"));
			parameter.setPhone(jsonObject.getString("phone"));
			parameter.setSex(jsonObject.getString("sex"));
			parameter.setType(jsonObject.getString("type"));
			parameter.setOwn_user_id(jsonObject.getString("own_user_id"));
			parameter.setOther_user_id(jsonObject.getString("other_user_id"));
			parameter.setAnother_user_id(jsonObject
					.getString("another_user_id"));
			parameter.setFrom_user_id(jsonObject.getString("from_user_id"));
			parameter.setTo_user_id(jsonObject.getString("to_user_id"));
			parameter.setShowEmail(jsonObject.getString("showEmail"));
			parameter.setShowPhone(jsonObject.getString("showPhone"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return parameter;
	}

}
