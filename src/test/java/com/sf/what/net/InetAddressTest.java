package com.sf.what.net;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.Test;

public class InetAddressTest {

	public static void main(String[] args) {
		
	}
	
	
	@Test
	public void test1() throws Exception {
		InetAddress localHost = InetAddress.getLocalHost();//获取本地主机
	
		System.out.println(localHost.getHostName());//HQSFTCSFIT0977A
		System.out.println(localHost.toString());//HQSFTCSFIT0977A/192.168.1.100
		System.out.println(localHost.getHostAddress());//192.168.1.100
		System.out.println(localHost.getCanonicalHostName());//HQSFTCSFIT0977A.sf.com
		System.out.println(localHost.isReachable(1000));//测试是否可以达到该地址。
		
	}
}
