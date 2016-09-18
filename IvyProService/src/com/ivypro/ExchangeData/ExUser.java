package com.ivypro.ExchangeData;

import java.util.Map;

import com.ivypro.Persistant.User;
import com.ivypro.Util.Json;

public class ExUser extends ExAbstract {

	private String id;

	private String age;

	private String name;

	private String password;

	private String introduce;

	private String address;

	private String iconUrl;

	private String email;

	private String phone;

	private String sex;

	public ExUser() {
		id = "";
		age = "";
		name = "";
		password = "";
		introduce = "";
		address = "";
		iconUrl = "";
		email = "";
		phone = "";
		sex = "";
	}

	public static final Short SEX_SHORT_MALE = 1;
	public static final Short SEX_SHORT_FEMALE = 2;
	public static final String SEX_STRING_MALE = "male";
	public static final String SEX_STRING_FEMALE = "female";

	public static String sexChange(Short sex) {
		if (sex == SEX_SHORT_MALE)
			return "male";
		else
			return "female";
	}

	public static Short sexChange(String sex) {
		if (SEX_STRING_MALE.equals(sex)) {
			return SEX_SHORT_MALE;
		} else
			return SEX_SHORT_FEMALE;
	}

	@Override
	public void setUpByPersistant(Object persistant) {
		if (persistant == null)
			return;
		User user = (User) persistant;
		id = String.valueOf(user.getId());
		age = String.valueOf(user.getAge());
		name = user.getName();
		password = user.getPassword();
		introduce = user.getIntroduce();
		address = user.getAddress();
		iconUrl = user.getIconUrl();
		email = user.getEmail();
		phone = user.getPhone();
		sex = sexChange(user.getSex());
	}

	@Override
	public Object toPersistant() {
		User user = new User();
		if (id != null && !"null".equals(id))
			user.setId(Integer.valueOf(id));
		try {
			user.setAge(Short.valueOf(age));
			user.setName(name);
			user.setIntroduce(introduce);
			user.setAddress(address);
			user.setIconUrl(iconUrl);
			user.setPhone(phone);
			user.setSex(sexChange(sex));
		} catch (Exception e) {
			// TODO
		}
		user.setEmail(email);
		user.setPassword(password);
		return user;
	}

	@Override
	public void setUpByJson(String json) {
		Map<String, String> map = Json.getMap(json);
		id = map.get("id");
		age = map.get("age");
		name = map.get("name");
		introduce = map.get("introduce");
		address = map.get("address");
		iconUrl = map.get("icon");
		email = map.get("email");
		phone = map.get("phone");
		password = map.get("password");
		sex = map.get("sex");
	}

	@Override
	public String toJson() {
		SuperMap map = new SuperMap();
		map.put("id", id);
		map.put("age", age);
		map.put("name", name);
		map.put("introduce", introduce);
		map.put("address", address);
		map.put("icon", iconUrl);
		map.put("email", email);
		map.put("phone", phone);
		map.put("sex", sex);
		map.put("password", password);
		return map.finishByJson();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setUpByMap(Map<String, String> map) {
		id = map.get("id");
		age = map.get("age");
		name = map.get("name");
		introduce = map.get("introduce");
		address = map.get("address");
		iconUrl = map.get("icon");
		email = map.get("email");
		phone = map.get("phone");
		password = map.get("password");
		sex = map.get("sex");
	}

}
