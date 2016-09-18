package com.melonstudio.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.melonstudio.util.MyLog;

public class User implements Serializable {

	private static final long serialVersionUID = -1496064829835540080L;

	private static final String TAG = "com.melonstudio.model.User";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private int user_id;

	private String age;

	private String username;

	private String name;

	private String introduce;

	private String iconUrl;

	private String email;

	private String phone;

	private String sex;

	private boolean showPhone;
	private boolean showEmail;

	public User() {
	}

	public User(JSONObject jsonObject) {
		try {
			user_id = jsonObject.getInt("user_id");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			iconUrl = jsonObject.getString("iconUrl");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			username = jsonObject.getString("username");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			name = jsonObject.getString("name");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			age = jsonObject.getString("age");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			introduce = jsonObject.getString("introduce");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			email = jsonObject.getString("email");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			phone = jsonObject.getString("phone");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			sex = jsonObject.getString("sex");
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			showEmail = Boolean.parseBoolean(jsonObject.getString("showEmail"));
		} catch (Exception e) {
			log(e.toString());
		}
		try {
			showPhone = Boolean.parseBoolean(jsonObject.getString("showPhone"));
		} catch (Exception e) {
			log(e.toString());
		}
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public boolean isShowPhone() {
		return showPhone;
	}

	public void setShowPhone(boolean showPhone) {
		this.showPhone = showPhone;
	}

	public boolean isShowEmail() {
		return showEmail;
	}

	public void setShowEmail(boolean showEmail) {
		this.showEmail = showEmail;
	}

}
