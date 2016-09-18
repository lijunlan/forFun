package com.sdll18.weixin.Deal.Tool.Impl;

import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import com.sdll18.weixin.Deal.Tool.MsgTool;
import com.sdll18.weixin.Deal.Util.XmlUtil;

public class LocationTool extends MsgTool {

	public LocationTool(Map<String, String> data) {
		super(data);
	}

	@Override
	public void doit() {
		resMap.put(
				"Content",
				"你的所在地维度为:" + mData.get("Location_X") + "\n" + "经度为:"
						+ mData.get("Location_Y") + "\n" + "具体位置为:"
						+ mData.get("Label"));
		try {
			resMsg = XmlUtil.writeXml(resMap);
		} catch (TransformerConfigurationException | SAXException e) {
			resMsg = "error";
			e.printStackTrace();
		}
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
