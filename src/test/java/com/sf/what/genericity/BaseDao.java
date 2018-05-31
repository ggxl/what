package com.sf.what.genericity;

import java.lang.reflect.ParameterizedType;

public class BaseDao<T> implements IBaseDao<T> {
	protected Class<T> entity;

	protected String entityName;
	
	@SuppressWarnings("unchecked")
	BaseDao(){
		this.entity = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.entityName = this.entity.getName();
		
		System.out.println("entity:"+this.entity);
		System.out.println("entityName:"+this.entityName);
	}
	public int add(T t) {
		System.out.println("add:"+t.getClass().getName());
		return 0;
	}

	public int delete(T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public T query(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
