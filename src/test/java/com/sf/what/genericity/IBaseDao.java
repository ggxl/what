package com.sf.what.genericity;

public interface IBaseDao<T> {
	int add(T t);
	int delete(T t);
	int update(T t);
	T query(T t);
}
