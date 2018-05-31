package com.sf.what.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * HttpClientUtil
 */
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	private static PoolingHttpClientConnectionManager cm;
	private static CookieStore cookieStore;
	private static String EMPTY_STR = "";
	private static CloseableHttpClient httpClient;
	
	private HttpClientUtil() {// "Utility class"
	}

	static {
		cookieStore = new BasicCookieStore();
		cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(50);// 整个连接池最大连接数
		cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2
		httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultCookieStore(cookieStore).build();
	}
	/**
	 * POST
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String doPost(String url) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		return getResult(httpPost);
	}
	/**
	 * POST
	 * @param url
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String doPost(String url,  Map<String, Object> params) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
		httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
		return getResult(httpPost);
	}
	/**
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	private static String doPost(String url,JSONObject params) {
		HttpPost post = new HttpPost(url);
		StringEntity entity = new StringEntity(params.toString(), "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		return getResult(post);
	}
	/**
	 * GET
	 * @param url
	 * @return
	 * @throws URISyntaxException
	 */
	public static String doGet(String url) throws URISyntaxException {
        HttpGet httpGet = new HttpGet(url);  
        return getResult(httpGet);  
	}
	/**
	 * GET
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	public static String doGet(String url, Map<String, Object> params) throws URISyntaxException {
		URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        return getResult(httpGet);  
	}
	/**
	 * 获取结果
	 * @param request
	 * @return
	 */
	private static String getResult(HttpRequestBase request) {
	
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				response.close();
				return result;
			}
		} catch (ClientProtocolException e) {
			logger.error("URI：{},HTTP协议异常:{}",request.getURI(),e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("URI：{},IO异常:{}",request.getURI(),e);
			e.printStackTrace();
		} 
		return EMPTY_STR;
	}
	
	private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {  
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();  
        for (Map.Entry<String, Object> param : params.entrySet()) {  
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));  
        }  
  
        return pairs;  
    }  
	
}
