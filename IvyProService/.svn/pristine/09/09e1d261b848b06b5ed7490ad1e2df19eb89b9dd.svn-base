package com.ivypro.Handle.Service;

import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Service.UserService;
import com.ivypro.Util.CheckUtil;
import com.ivypro.Util.MD5Util;
import com.ivypro.Util.MsgUtil;
import com.ivypro.Util.RSAUtil;

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
		return getData().containsKey("email")
				&& getData().containsKey("password");
	}

	@Override
	public void doit() {
		String email = getData().get("email");
		String password = getData().get("password");
		if (!CheckUtil.checkEmail(email) || "".equals(password)) {
			setResMsg(MsgUtil.getErrorMsg("email or password is wrong"));
			return;
		}
		try {
			password = RSAUtil.decryptStr(password, RSAUtil.RSAKEY_BASE_PATH);
		} catch (Exception e1) {
			e1.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("error"));
			return;
		}
		getData().put("password", MD5Util.MD5(password));
		ExUser exUser = new ExUser();
		exUser.setUpByMap(getData());
		try {
			getUserService().create(exUser);
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg(e.getMessage()));
			return;
		}
		setResMsg(MsgUtil.getSuccessMsg("register successfully"));
	}
}
