package com.sf.what.time;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimeUnitTest {

	@Test
	public void test1() throws Exception {
		//将一分钟转化成秒
		System.out.println(TimeUnit.MINUTES.toSeconds(1));//60
		TimeUnit.MINUTES.sleep(3);//睡眠3分钟。相比Thread.sleep(1000*60*3);可读性更高
		
	}
}
