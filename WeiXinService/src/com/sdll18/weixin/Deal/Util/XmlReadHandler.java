package com.sdll18.weixin.Deal.Util;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sdll18.weixin.Deal.Util.XmlUtil.ReadXmlListener;

public class XmlReadHandler extends DefaultHandler {

	private ReadXmlListener listener;
	private Map<String, String> mData;
	private String tagName;

	public XmlReadHandler(ReadXmlListener l) {
		listener = l;
	}

	@Override
	public void startDocument() throws SAXException {
		mData = new HashMap<String, String>();
	}

	@Override
	public void endDocument() throws SAXException {
		listener.onFinished(mData);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = qName;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tagName = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tagName != null) {
			String data = new String(ch, start, length);
			mData.put(tagName, data);
		}
	}
}
