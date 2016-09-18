package com.sdll18.umedia.Deal.Service;

import com.sdll18.umedia.Data.MsgUtil;
import com.sdll18.umedia.Persistant.User;
import com.sdll18.umedia.Service.UserService;

public class RegisterService extends MsgService {

	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected boolean checkData() {
		return getData().containsKey("username")
				&& getData().containsKey("password")
				&& getData().containsKey("url")
				&& getData().containsKey("email")
				&& getData().containsKey("name");
	}

	@Override
	public void doit() {
		User user = new User();
		user.setEmail(getData().get("email"));
		user.setIconUrl(getData().get("url"));
		user.setName(getData().get("name"));
		user.setUsername(getData().get("username"));
		user.setPassword(getData().get("password"));
		getUserService().create(user);
		setResMsg(MsgUtil.getSuccessMsg("register successfully"));
	}

}
