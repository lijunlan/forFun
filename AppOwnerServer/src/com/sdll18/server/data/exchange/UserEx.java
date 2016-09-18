package com.sdll18.server.data.exchange;

import java.util.Map;

import com.sdll18.server.data.persistence.User;
import com.sdll18.server.network.data.SuperMap;
import com.sdll18.server.network.json.Json;

public class UserEx {

	private int id;

	private short state;

	private short age;

	private String username;

	private String password;

	private String name;

	private String introduce;

	private String iconUrl;

	private String email;

	private String phone;

	private short sex;

	public UserEx(User user) {
		id = user.getId();
		state = user.getState();
		age = user.getAge();
		username = user.getUsername();
		password = user.getPassword();
		name = user.getName();
		introduce = user.getIntroduce();
		iconUrl = user.getIconUrl();
		email = user.getEmail();
		phone = user.getPhone();
		sex = user.getSex();
	}

	public UserEx(String json) {
		Map<String, String> map = Json.getMap(json);
		id = Integer.valueOf(map.get("id"));
		state = Short.valueOf(map.get("state"));
		age = Short.valueOf(map.get("age"));
		username = map.get("username");
		password = map.get("password");
		name = map.get("name");
		introduce = map.get("introduce");
		iconUrl = map.get("iconUrl");
		email = map.get("email");
		phone = map.get("phone");
		sex = Short.valueOf(map.get("sex"));
	}

	public User getUser() {
		User user = new User();
		user.setId(id);
		user.setSex(sex);
		user.setState(state);
		user.setAge(age);
		user.setUsername(username);
		user.setPassword(password);
		user.setName(name);
		user.setIntroduce(introduce);
		user.setIconUrl(iconUrl);
		user.setEmail(email);
		user.setPhone(phone);
		return user;
	}

	public String getJson() {
		SuperMap map = new SuperMap("user");
		map.put("id", id);
		map.put("state", state);
		map.put("age", age);
		map.put("username", username);
		map.put("password", password);
		map.put("introduce", introduce);
		map.put("name", name);
		map.put("iconUrl", iconUrl);
		map.put("email", email);
		map.put("phone", phone);
		map.put("sex", sex);
		return map.finishByJson();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public short getAge() {
		return age;
	}

	public void setAge(short age) {
		this.age = age;
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

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

}
