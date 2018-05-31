package com.sf.what.string;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 字符串研究
 */
public class StringTest {

	public static void main(String[] args) {
		
		
	}
	/**
	 * intern 方法  返回一个字符串，内容与此字符串相同，但它保证来自字符串池中。
	 * 当调用 intern 方法时，如果池已经包含一个等于此 String 对象的字符串（该对象由 equals(Object) 方法确定），则返回池中的字符串。
	 * 否则，将此 String 对象添加到池中，并且返回此 String 对象的引用
	 * 字符串常量池
	 */
	@Test
	public void test1(){
		
		String str1 = new String("a")+new String("bc");//此时new关键字在堆中创建了abc字符串对象
		
		String str = str1.intern();//从池中返回字符串对象，如果池中没有此字符串对象，先添加到池中在返回，如果有直接返回
		//jdk1.6的做法是直接在常量池中生成一个 "abc" 的对象。
		//jdk1.7及以上版本  常量池中不需要再存储一份对象了，可以直接存储堆中的引用。这份引用直接指向 str1 引用的对象，也就是说str1.intern() ==str1会返回true。
		String str2 = "abc";//因此此时abc和str1是同一个引用
		System.out.println(str == str1);//true  因为字符串常量池中的abc是由堆中的abc拷贝来的所有为true，相当于只在堆中做了创建，所以是同一份abc
		System.out.println(str1 == str2);//true
		System.out.println(str == str2);//true
	}
	/**
	 * intern 方法
	 * 字符串常量池
	 */
	@Test
	public void test2() {
		
		String str1 = "abc";//先在字符串常量池中创建abc
		String str2 = new String("a")+new String("ab");//在堆中创建abc
		
		String str = str1.intern();//如果字符串常量池中有abc则从字符串常量池中返回abc，如果没有则添加到常量池并返回
		System.out.println(str == str1);//true   事先在常量池中创建了abc
		System.out.println(str1 == str2);//false  str2是堆中的abc，常量池中已经创建了abc堆中的abc不会被在次添加到常量池中所以false
		System.out.println(str == str2);//false
	}
	
	/**
	 * 验证jdk1.6和1.7的区别
	 *jdk1.6的做法是直接在常量池中生成一个 "abc" 的对象。
	 *jdk1.7及以上版本  常量池中不需要再存储一份对象了，可以直接存储堆中的引用。这份引用直接指向 str1 引用的对象，也就是说str1.intern() ==str1会返回true。
	 */
	@Test
	public void test3() throws Exception {
		String s = new String("ab")+new String("c");
		String s2 = s.intern();
		System.out.println(s == s2);//jdk1.6  false  jdk1.7及以上 true
	}
	
	@Test
	public void test4() throws Exception {
		String str1 = "a";
		String str2 = "b";
		String str3 = "ab";
		String str4 = str1+str2; //相当于是在堆中创建的ab
		String str5 = "a"+"b"; //只有用引号包含文本的方式创建的String对象之间使用"+"连接产生的新对象才会被加入在字符串常量池中
		String str6 = new String("ab");
		System.out.println(str6.intern()==str3);//true
		System.out.println(str6.intern()==str4);//false
		System.out.println(str6.intern()==str5);//true
		System.out.println(str4 ==str3);//false
		System.out.println(str5 ==str3);//true
		
	}
	
	@Test
	public void test5() throws Exception {
		Integer i1 = new Integer(1);
		Integer i2 = new Integer(1);
		Integer i3 = 1;
		Integer i4 = 1;
		System.out.println(i1==i2);//false
		System.out.println(i1.equals(i2));//true
		System.out.println(i1==i3);//false
		System.out.println(i4==i3);//true
		
	}
	
	
	
	
}
