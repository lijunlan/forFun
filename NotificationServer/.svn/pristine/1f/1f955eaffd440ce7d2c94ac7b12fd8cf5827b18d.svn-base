package com.sdll18.notificationserver.network.service;

import java.util.Date;

import com.sdll18.notificationserver.network.UserManager;
import com.sdll18.notificationserver.network.data.ExUtil;
import com.sdll18.notificationserver.network.data.SuperMap;

public class MinaStartService extends MsgService {

	@Override
	protected boolean checkData() {
		return getData().containsKey("user_id")
				&& getData().containsKey("refresh_time")
				&& getData().containsKey("formulate_time");
	}

	@Override
	public void doit() {
		try {
			String user_id = getData().get("user_id");
			UserManager.getManager().addUser(user_id, getSession());
			boolean isFormulateTime = Boolean.parseBoolean(getData().get(
					"formulate_time"));
			SuperMap send = new SuperMap();
			send.put("state", "success")
					.put("method", "mina_start")
					.put("msg", "started successfully")
					.put("time",
							ExUtil.time2String("" + new Date().getTime(),
									isFormulateTime));
			setResMsg(send.finishByJson());
		} catch (Exception e) {
			e.printStackTrace();
			SuperMap temp = new SuperMap();
			temp.put("state", "error").put("msg", e.getMessage());
			setResMsg(temp.finishByJson());
		}
	}

}
