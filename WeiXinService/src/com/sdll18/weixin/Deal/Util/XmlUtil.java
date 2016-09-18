package com.sdll18.weixin.Deal.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

public class XmlUtil {

	public interface ReadXmlListener {

		void onFinished(Map<String, String> data);

		void onError(String msg);
	}

	public static void readXml(String xml, ReadXmlListener listener)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser = saxPF.newSAXParser();
		InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		saxParser.parse(is, new XmlReadHandler(listener));
	}

	// TODO
	public static String writeXml(Map<String, String> data)
			throws TransformerConfigurationException, SAXException {
		SAXTransformerFactory saxTF = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler transformerHandler = saxTF.newTransformerHandler();
		Transformer transformer = transformerHandler.getTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		StringWriter stringWriter = new StringWriter();
		transformerHandler.setResult(new StreamResult(stringWriter));
		transformerHandler.startDocument();
		transformerHandler.startElement("", "xml", "xml", null);
		Set<String> keys = data.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			String key = it.next();
			transformerHandler.startElement("", key, key, null);
			char[] detail = data.get(key).toCharArray();
			transformerHandler.characters(detail, 0, detail.length);
			transformerHandler.endElement("", key, key);
		}
		transformerHandler.endElement("", "xml", "xml");
		transformerHandler.endDocument();
		return stringWriter.toString();
	}
}
