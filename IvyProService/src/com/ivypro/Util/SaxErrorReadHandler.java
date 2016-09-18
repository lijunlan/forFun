package com.ivypro.Util;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxErrorReadHandler extends DefaultHandler {

	private Map<String, String> settingData;
	private String tagName;

	public SaxErrorReadHandler(Map<String, String> s) {
		settingData = s;
	}

	public void startDocument() throws SAXException {
		// 开始解析
	}

	public void endDocument() throws SAXException {
		// 解析结束
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// System.out.println(localName + "\t" + qName);
		tagName = qName;
		String value = attributes.getValue("value");
		settingData.put(tagName, value);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//DO NOTHING
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}
}
