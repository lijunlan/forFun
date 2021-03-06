package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Data.SuperMap;
import com.sdll18.umedia.Persistant.User;
import com.sdll18.umedia.Protocol.Protocol;
import com.sdll18.umedia.Service.UserService;

public class DownUserInfoService extends MsgService {

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("user_id");
	}

	public static String getSex(Short s) {
		if (s == null)
			return "";
		if (s == Protocol.SEX_FEMALE)
			return "女";
		if (s == Protocol.SEX_MALE)
			return "男";
		return "未知";
	}

	@Override
	public void doit() {
		int id = Integer.parseInt(getData().get("user_id"));
		User user = getUserService().query(id, false);
		if (user == null) {
			setResMsg(MsgUtil.getErrorMsg("user is not existed"));
			return;
		}
		SuperMap data = new SuperMap();
		data.put("username", user.getUsername());
		data.put("name", user.getName());
		data.put("age", user.getAge());
		data.put("realname", user.getRealName());
		data.put("address", user.getAddress());
		data.put("sex", getSex(user.getSex()));
		data.put("birthday", user.getBirthday());
		data.put("bloodType", user.getBloodType());
		data.put("introduce", user.getIntroduce());
		data.put("phone", user.getPhone());
		data.put("email", user.getEmail());
		data.put("qq", user.getQq());
		data.put("url", user.getIconUrl());
		setResMsg(data.finishByJson());
	}

}
