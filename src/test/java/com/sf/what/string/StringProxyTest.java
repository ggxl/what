package com.sf.what.string;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StringProxyTest {

	public static void main(String[] args) {
		
		/**
		 * 本来想测试一下String常量池会不会被垃圾回收
		 * 但String 无法被继承，无法重写finalize方法验证
		 */
		
		
		final T t = new T();
		Itf f = (Itf)Proxy.newProxyInstance(T.class.getClassLoader(),T.class.getInterfaces(), new InvocationHandler(){

			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
				System.out.println(proxy.getClass().getName());
				System.out.println("----------------");
				method.invoke(t, args);
				System.out.println("----------------");
				return null;
			}
			
		});
		
		f.m1();
	
		
	}
	
	
}



interface Itf{
	void m1();
}
class T implements Itf{
	public void m1(){
		System.out.println("---------------T m1 run --------------");
	}
}