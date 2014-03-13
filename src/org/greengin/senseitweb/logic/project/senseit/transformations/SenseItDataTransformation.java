package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.HashMap;

public class SenseItDataTransformation {
	private String name;
	private HashMap<String, String> data;
	private HashMap<String, Integer> units;
	
	public SenseItDataTransformation() {
		name = null;
		data = new HashMap<String, String>();
		units = new HashMap<String, Integer>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, String> getData() {
		return data;
	}
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	public HashMap<String, Integer> getUnits() {
		return units;
	}
	public void setUnits(HashMap<String, Integer> units) {
		this.units = units;
	}
}