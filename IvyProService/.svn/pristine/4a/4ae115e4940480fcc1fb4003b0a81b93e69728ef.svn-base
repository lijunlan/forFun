package com.ivypro.Handle.Service;

import java.util.UUID;

import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Service.UserService;
import com.ivypro.Util.MD5Util;
import com.ivypro.Util.MsgUtil;
import com.ivypro.Util.RSAUtil;

public class LoginService extends MsgService {

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
		try {
			password = RSAUtil.decryptStr(password, RSAUtil.RSAKEY_BASE_PATH);
			password = MD5Util.MD5(password);
		} catch (Exception e1) {
			e1.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("error"));
			return;
		}
		ExUser exUser;
		try {
			exUser = getUserService().query(email, false);
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg("email is not exsited"));
			return;
		}
		if (password.equals(exUser.getPassword())) {
			String _UUID = UUID.randomUUID().toString();
			getUserMarkService().create(exUser.getId(), _UUID);
			setResMsg("{'mark':'" + _UUID + "','state':'success'}");
		} else {
			setResMsg(MsgUtil.getErrorMsg("password is not accurate"));
		}
	}

}
