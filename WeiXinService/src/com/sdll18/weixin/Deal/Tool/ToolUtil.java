package com.sdll18.weixin.Deal.Tool;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.sdll18.weixin.Deal.Tool.Impl.EventTool;
import com.sdll18.weixin.Deal.Tool.Impl.LocationTool;
import com.sdll18.weixin.Deal.Tool.Impl.TextTool;

public class ToolUtil {

	private static final String IMAGE = "image";
	private static final String TEXT = "text";
	private static final String VOICE = "voice";
	private static final String VIDEO = "video";
	private static final String LOCATION = "location";
	private static final String EVENT = "event";

	public static void deal(Map<String, String> data,
			HttpServletResponse response) {
		MsgTool tool = chooseType(data);
		if (tool != null) {
			tool.doit();
			String msg = tool.getResponseMsg();
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			try {
				OutputStream stream = response.getOutputStream();
				System.out.println(msg);
				stream.write(msg.getBytes("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static MsgTool chooseType(Map<String, String> data) {
		MsgTool msgTool = null;
		String type = data.get("MsgType");
		switch (type) {
		case TEXT:
			msgTool = new TextTool(data);
			break;
		case IMAGE:

			break;
		case VOICE:

			break;
		case VIDEO:

			break;
		case LOCATION:
			msgTool = new LocationTool(data);
			break;
		case EVENT:
			msgTool = new EventTool(data);
			break;
		default:
			// TODO unknown type
			break;
		}
		return msgTool;
	}
}
