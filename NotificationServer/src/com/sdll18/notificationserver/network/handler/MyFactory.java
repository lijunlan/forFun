package com.sdll18.notificationserver.network.handler;

import java.util.Map;

import com.sdll18.notificationserver.network.data.SuperMap;
import com.sdll18.notificationserver.network.service.MinaHeartService;
import com.sdll18.notificationserver.network.service.MinaStartService;
import com.sdll18.notificationserver.network.service.MsgService;
import com.sdll18.notificationserver.network.service.SendMessageService;
import com.sdll18.notificationserver.network.service.SendNotificationService;
import com.sdll18.notificationserver.network.service.VerifyServerService;

public class MyFactory {

	public static MsgService produce(Map<String, String> data){
		if (data.containsKey("style") && data.containsKey("method")) {
			String style = data.get("style");
			String method = data.get("method");
			if (style.equals("mina")) {
				if (method.equals("mina_heart")) {
					return new MinaHeartService();
				} else if (method.equals("mina_start")) {
					return new MinaStartService();
				} else if (method.equals("send_message")) {
					return new SendMessageService();
				} else if (method.equals("send_notification")) {
					return new SendNotificationService();
				} else if (method.equals("verify_server")) {
					return new VerifyServerService();
				} else {
					System.out.println("method is not accurate"); 
					return null;
				}
			} else {
				System.out.println("style is not accurate"); 
				return null;
			}
		} else {
			System.out.println("style or method is expected"); 
			return null;
		}
	}
}
