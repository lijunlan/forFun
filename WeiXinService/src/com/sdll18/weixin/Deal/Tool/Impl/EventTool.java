package com.sdll18.weixin.Deal.Tool.Impl;

import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import com.sdll18.weixin.Deal.Tool.MsgTool;
import com.sdll18.weixin.Deal.Util.XmlUtil;

public class EventTool extends MsgTool {

	private static final String SUBSCRIBE = "subscribe";
	private static final String UNSUBSCRIBE = "unsubscribe";
	private static final String LOCATION = "LOCATION";

	public EventTool(Map<String, String> data) {
		super(data);
	}

	@Override
	public void doit() {
		String eventType = mData.get("Event");
		switch (eventType) {
		case SUBSCRIBE:
			resMap.put("Content", "欢迎关注SDLL18的小助手，更多精彩功能正在开发中哦~");
			break;
		case UNSUBSCRIBE:
			resMap.put("Content", "欢迎关注SDLL18的小助手，更多精彩功能正在开发中哦~");
			break;
		case LOCATION:
			resMap.put(
					"Content",
					"你的所在地维度为:" + mData.get("Latitude") + "\n" + "经度为:"
							+ mData.get("Longitude") + "\n" + "精确度:"
							+ mData.get("Precision"));
			break;
		default:
			resMap.put("Content", "微信发来了一个奇怪的东西=.=");
			break;
		}
		try {
			resMsg = XmlUtil.writeXml(resMap);
		} catch (TransformerConfigurationException | SAXException e) {
			resMsg = "error";
			e.printStackTrace();
		}
	}

	@Override
	public String getResponseMsg() {
		return resMsg;
	}

	@Override
	protected void setUpMap() {
		super.setUpMap();
		resMap.put("ToUserName", mData.get("FromUserName"));
		resMap.put("FromUserName", mData.get("ToUserName"));
		resMap.put("CreateTime",
				String.valueOf(Calendar.getInstance().getTimeInMillis()));
		resMap.put("MsgType", "text");
	}

}
