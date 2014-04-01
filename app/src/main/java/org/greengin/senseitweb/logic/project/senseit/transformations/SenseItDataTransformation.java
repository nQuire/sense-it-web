package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.HashMap;

public class SenseItDataTransformation {
	private String name;
	private HashMap<String, String> data;
	private SenseItDataUnits units;
	
	public SenseItDataTransformation() {
		name = null;
		data = new HashMap<String, String>();
		units = new SenseItDataUnits();
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
	public SenseItDataUnits getUnits() {
		return units;
	}
	public void setUnits(SenseItDataUnits units) {
		this.units = units;
	}
}