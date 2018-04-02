package com.et.extract.parser;

import java.util.Enumeration;
import java.util.Properties;

public class ExtractFactory {

	public static Extract getExtract(Properties props, String extractText) throws Exception {
		Enumeration<Object> keyEnumeration = props.keys();
		
		while (keyEnumeration.hasMoreElements()) {
			String key = (String) keyEnumeration.nextElement();
			String value = props.getProperty(key);
			if (key.contains("_Extract_Keyword")) {
				for (String s : extractText.split("\\r\\n")) {
					if (s.equals(value)) {
						String className = props.getProperty(key.split("_")[0] + "_Extract_Class");
						return (Extract) Class.forName(className).getDeclaredConstructor().newInstance();
					}
				}
			}
		}
		return null;
	}

}
