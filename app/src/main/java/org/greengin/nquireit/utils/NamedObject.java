package org.greengin.nquireit.utils;

public class NamedObject<T> {
	public String title;
	public T obj;
	
	public NamedObject(String title, T obj) {
		super();
		this.title = title;
		this.obj = obj;
	}
		
}
