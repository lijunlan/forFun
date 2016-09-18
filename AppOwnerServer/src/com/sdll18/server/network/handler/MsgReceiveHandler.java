package com.sdll18.server.network.handler;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.data.MessageStyle;
import com.sdll18.server.network.data.SuperMap;
import com.sdll18.server.network.handler.util.CommentHandler;
import com.sdll18.server.network.handler.util.GroupHandler;
import com.sdll18.server.network.handler.util.MsgHandler;
import com.sdll18.server.network.handler.util.NewsHandler;
import com.sdll18.server.network.handler.util.NotificationHandler;
import com.sdll18.server.network.handler.util.UserHandler;
import com.sdll18.server.network.json.Json;

public class MsgReceiveHandler {

	public static void handle(IoSession session, Object message) {
		Map<String, String> map = Json.getMap((String) message);
		String s = map.get("style");
		MsgHandler mHandler = null;
		mHandler = getHandlerInstance(s,session,map);
		if (mHandler != null) {
			mHandler.doit();
		} else {
			session.write(new SuperMap("error").put("info",
					"style is undefined!").finishByJson());
		}
	}

	private static MsgHandler getHandlerInstance(String s,IoSession session,Map<String, String> map) {
		MsgHandler mHandler;
		switch (s) {
		case MessageStyle.MESSAGE_STYLE_COMMENT:
			mHandler = new CommentHandler(session,map);
			break;
		case MessageStyle.MESSAGE_STYLE_USER:
			mHandler = new UserHandler(session,map);
			break;
		case MessageStyle.MESSAGE_STYLE_NEWS:
			mHandler = new NewsHandler(session,map);
			break;
		case MessageStyle.MESSAGE_STYLE_GROUP:
			mHandler = new GroupHandler(session,map);
			break;
		case MessageStyle.MESSAGE_STYLE_NOTIFICATION:
			mHandler = new NotificationHandler(session,map);
			break;
		default:
			mHandler = null;
			break;
		}
		return mHandler;
	}
}
