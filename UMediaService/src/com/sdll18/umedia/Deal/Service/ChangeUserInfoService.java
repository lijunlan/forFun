package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Persistant.User;
import com.sdll18.umedia.Protocol.Protocol;
import com.sdll18.umedia.Service.UserService;

public class ChangeUserInfoService extends MsgService {

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("user_id")
				&& (getData().containsKey("email")
						|| getData().containsKey("name")
						|| getData().containsKey("realname")
						|| getData().containsKey("age")
						|| getData().containsKey("phone")
						|| getData().containsKey("address")
						|| getData().containsKey("sex")
						|| getData().containsKey("birthday")
						|| getData().containsKey("bloodtype")
						|| getData().containsKey("introduce") || getData()
						.containsKey("qq"));
	}

	@Override
	public void doit() {
		int id = Integer.parseInt(getData().get("user_id"));
		User user = getUserService().query(id, false);
		if (user == null) {
			setResMsg(MsgUtil.getErrorMsg("user is not existed"));
			return;
		}
		if (getData().containsKey("email")) {
			user.setEmail(getData().get("email"));
		}
		if (getData().containsKey("name")) {
			user.setName(getData().get("name"));
		}
		if (getData().containsKey("realname")) {
			user.setRealName(getData().get("realname"));
		}
		if (getData().containsKey("age")) {
			user.setAge(Short.valueOf(getData().get("age")));
		}
		if (getData().containsKey("phone")) {
			user.setPhone(getData().get("phone"));
		}
		if (getData().containsKey("address")) {
			user.setAddress(getData().get("address"));
		}
		if (getData().containsKey("birthday")) {
			user.setBirthday(getData().get("birthday"));
		}
		if (getData().containsKey("bloodtype")) {
			user.setBloodType(getData().get("bloodtype"));
		}
		if (getData().containsKey("introduce")) {
			user.setIntroduce(getData().get("introduce"));
		}
		if (getData().containsKey("qq")) {
			user.setQq(getData().get("qq"));
		}
		if (getData().containsKey("sex")) {
			user.setSex(getSex(getData().get("sex")));
		}
		getUserService().update(user);
		setResMsg(MsgUtil.getSuccessMsg("update user info successfully"));
	}

	private static Short getSex(String s) {
		if (s.equals("女")) {
			return Protocol.SEX_FEMALE;
		}
		if (s.equals("男")) {
			return Protocol.SEX_MALE;
		}
		return 2;
	}

}
