package com.ivypro.Handle.Service;

import com.ivypro.ExchangeData.ExUser;
import com.ivypro.Util.MsgUtil;

public class GetUserInfoService extends MsgService {

	@Override
	protected boolean checkData() {
		return getData().containsKey("mark");
	}

	@Override
	public void doit() {
		String mark = getData().get("mark");
		ExUser exUser = null;
		try {
			exUser = getUserMarkService().queryUser(mark);
		} catch (Exception e) {
			e.printStackTrace();
			setResMsg(MsgUtil.getErrorMsg(e.getMessage()));
			return;
		}
		setResMsg("{'state':'success','email':'" + exUser.getEmail() + "'}");
	}

}
