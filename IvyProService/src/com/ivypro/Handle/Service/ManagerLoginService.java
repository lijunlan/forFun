package com.ivypro.Handle.Service;

import com.ivypro.Util.MsgUtil;

public class ManagerLoginService extends MsgService {

	// TODO
	@Override
	protected boolean checkData() {
		return getData().containsKey("username")
				&& getData().containsKey("password");
	}

	@Override
	public void doit() {
		String username = getData().get("username");
		String password = getData().get("password");
		if ("ivypro".equals(username) && "huzhongkai520".equals(password)) {
			setResMsg("{'managerId':'" + 88889999 + "','state':'success'}");
		} else {
			setResMsg(MsgUtil.getErrorMsg("password is not accurate"));
		}
	}

}
