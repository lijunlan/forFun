package com.sdll18.weixin.Deal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.sdll18.weixin.Deal.Tool.ToolUtil;
import com.sdll18.weixin.Deal.Util.XmlUtil;
import com.sdll18.weixin.Deal.Util.XmlUtil.ReadXmlListener;

public class DealMsgIn {

	public static void deal(HttpServletRequest rq, HttpServletResponse rp) {
		DealMsgIn msgIn = new DealMsgIn(rq, rp);
		try {
			msgIn.Xml2Map();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private HttpServletRequest req;
	private HttpServletResponse resp;

	private DealMsgIn(HttpServletRequest rq, HttpServletResponse rp) {
		req = rq;
		resp = rp;
	}

	private void Xml2Map() throws UnsupportedEncodingException,
			ParserConfigurationException, SAXException, IOException {
		req.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				req.getInputStream(), "UTF-8"));
		String line = "";
		StringBuffer buffer = new StringBuffer();
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		System.out.println(buffer.toString());
		XmlUtil.readXml(buffer.toString(), new ReadXmlListener() {

			@Override
			public void onFinished(Map<String, String> data) {
				dealData(data);
			}

			@Override
			public void onError(String msg) {
				// TODO
			}
		});
	}

	private void dealData(Map<String, String> data) {
		ToolUtil.deal(data, resp);
	}

}
