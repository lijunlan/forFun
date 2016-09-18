package com.sdll18.server.spring;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class SpringUtil {

	private static XmlBeanFactory singleInstance;

	public XmlBeanFactory getBeanFactory() {
		if (singleInstance == null) {
			FileSystemResource fsr = new FileSystemResource(
					"applicationContext.xml");
			singleInstance = new XmlBeanFactory(fsr);
		}
		return singleInstance;
	}

}
