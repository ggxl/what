package com.sf.what.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexpTest {

	@Test
	public void test1() throws Exception {
		
		//ESG_ACSP_CORE_CNSZ22_JETTY_WEB_TOOL_01
		String serverPath = "/app/jetty/server/ESG_ACSP_CORE_CNSZ22_JETTY_WEB_TOOL_01/resources";
				String pattern = ".*/(.*WEB_TOOL_[^/]*)/.*";
				Pattern compile = Pattern.compile(pattern);
				Matcher matcher = compile.matcher(serverPath);
				String configPath = null;
				if (matcher.find()) {
					System.out.println(matcher.group(1));
					configPath = "/app/war/"+matcher.group(1)+"/resources";
					
					System.out.println("------------configPath----------:"+configPath);
				}
	}
}
