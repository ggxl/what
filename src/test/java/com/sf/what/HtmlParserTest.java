package com.sf.what;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;

import com.sf.what.util.HttpClientUtil;

public class HtmlParserTest {

	
	
	@Test
	public void test(){
		String url = "https://cas.sf-express.com/cas/login?service=http%3A%2F%2Fhos.sf-express.com%2Fframe.pvt";
		try {
			String doGet = HttpClientUtil.doGet(url);
			System.out.println(doGet);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Test
	public void test2(){
		String url = "https://cas.sf-express.com/cas/imgcode?a="+Math.random();
		
		
		
	}
	@Test
	public void test3() throws ParserException, MalformedURLException, IOException, URISyntaxException{
		String url = "http://10.118.88.200:8099/portal/openapi/login_success_show.htm?wlanu=175515728";
		String doGet = HttpClientUtil.doGet(url);
		Parser parser = new Parser(doGet/*((HttpURLConnection)(new URL(url)).openConnection())*/);
		
		NodeFilter filter = new HasAttributeFilter("id","downline");
		
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		Node node = nodeList.elementAt(0);
		String msg = node.toHtml();
		
		System.out.println(msg);
	}
}
