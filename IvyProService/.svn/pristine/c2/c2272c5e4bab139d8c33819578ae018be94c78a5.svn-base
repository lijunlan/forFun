package com.ivypro.Handle.Service;

import com.ivypro.Util.MsgUtil;

public class LogoutService extends MsgService {

	@Override
	protected boolean checkData() {
		return getData().containsKey("mark");
	}

	@Override
	public void doit() {
		String UUID = getData().get("mark");
		getUserMarkService().remove(UUID);
		setResMsg(MsgUtil.getSuccessMsg("logout successfully"));
	}

}
