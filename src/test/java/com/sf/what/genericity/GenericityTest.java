package com.sf.what.genericity;

import com.sf.what.domain.User;

public class GenericityTest {

	public static void main(String[] args) {
		UserDao userDao = new UserDao();
		User user = new User();
		userDao.add(user);
		
		
	}
	
	
	
}
