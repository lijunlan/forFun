package com.sdll18.weixin.Deal.Tool.Impl;

import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import com.sdll18.weixin.Deal.Tool.MsgTool;
import com.sdll18.weixin.Deal.Util.XmlUtil;

public class TextTool extends MsgTool {

	public TextTool(Map<String, String> data) {
		super(data);
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

	@Override
	public void doit() {
		String inMsg = mData.get("Content");
		if (inMsg.contains("天气") || inMsg.contains("weather")) {
			// TODO
			resMap.put("Content", "对不起，亲，天气系统正在开发中=。=");
		} else {
			resMap.put("Content", "我能学你说话哦，你看\n" + inMsg);
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
}
