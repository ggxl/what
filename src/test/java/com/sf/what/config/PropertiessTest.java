package com.sf.what.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiessTest {

	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		InputStream inStream = new FileInputStream("E://confa.properties");
		properties.load(inStream);
		System.out.println(properties.getProperty("key"));
		
		
	}
}
