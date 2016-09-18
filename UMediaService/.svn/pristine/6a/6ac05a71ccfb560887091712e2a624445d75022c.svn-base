package com.sdll18.umedia.PackUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class WriteXmlUtil {

	public static void main(String[] args) throws IOException,
			TransformerConfigurationException, SAXException {
		File file = new File("/Users/SDLL18/Desktop/test.xml");
		Map<String, String> sd = new HashMap<String, String>();
		sd.put("host", "http://www.sdll18.com");
		sd.put("hostName", "AppFactoryService");
		sd.put("cachePath", "/WebData/");
		sd.put("id", "1");
		Map<String, Map<String, String>> cd = new HashMap<String, Map<String, String>>();
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put("cancel_praise", "deletePraiseService");
		tmp.put("praise", "upPraiseService");
		cd.put("user", tmp);
		tmp = new HashMap<String, String>();
		tmp.put("get_comment_list", "downCommentService");
		cd.put("comment", tmp);
		writeXml(file, cd, sd);
	}

	public static void writeXml(File file,
			Map<String, Map<String, String>> configData,
			Map<String, String> settingData) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			fw = new FileWriter(file);
			pw = new PrintWriter(fw);
			pw.write(getXml(configData, settingData));
			pw.flush();
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pw != null)
					pw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getXml(Map<String, Map<String, String>> configData,
			Map<String, String> settingData)
			throws TransformerConfigurationException, SAXException {
		SAXTransformerFactory saxTF = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler transformerHandler = saxTF.newTransformerHandler();
		Transformer transformer = transformerHandler.getTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		StringWriter stringWriter = new StringWriter();
		transformerHandler.setResult(new StreamResult(stringWriter));
		transformerHandler.startDocument();
		transformerHandler.startElement("", "service-configuration",
				"service-configuration", null);
		writeSetting(transformerHandler, settingData);
		writeConfig(transformerHandler, configData);
		transformerHandler.endElement("", "service-configuration",
				"service-configuration");
		transformerHandler.endDocument();
		return stringWriter.toString();
	}

	private static void writeConfig(TransformerHandler handler,
			Map<String, Map<String, String>> configData) throws SAXException {
		Iterator<String> it = configData.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Map<String, String> data = configData.get(key);
			AttributesImpl attr = new AttributesImpl();
			attr.addAttribute(null, "name", "name", String.class.getName(), key);
			handler.startElement("", "style", "style", attr);
			Iterator<String> itr = data.keySet().iterator();
			while (itr.hasNext()) {
				String k = itr.next();
				String d = data.get(k);
				AttributesImpl a = new AttributesImpl();
				a.addAttribute(null, "name", "name", String.class.getName(), k);
				a.addAttribute(null, "beanName", "beanName",
						String.class.getName(), d);
				handler.startElement("", "method", "method", a);
				handler.endElement("", "method", "method");
			}
			handler.endElement("", "style", "style");
		}
	}

	private static void writeSetting(TransformerHandler handler,
			Map<String, String> settingData) throws SAXException {
		Iterator<String> it = settingData.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String data = settingData.get(key);
			AttributesImpl attr = new AttributesImpl();
			attr.addAttribute(null, "value", "value", String.class.getName(),
					data);
			handler.startElement("", key, key, attr);
			handler.endElement("", key, key);
		}
	}
}
