package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.Vector;

public class SenseItDataSensor {
	
	public SenseItDataSensor() {
		this.name = null;
		this.output = null;
		this.labels = new Vector<String>();
		this.units = new SenseItDataUnits();
	}
	
	String name;
	String output;
	Vector<String> labels;
	SenseItDataUnits units;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public Vector<String> getLabels() {
		return labels;
	}
	public void setLabels(Vector<String> labels) {
		this.labels = labels;
	}
	public SenseItDataUnits getUnits() {
		return units;
	}
	public void setUnits(SenseItDataUnits units) {
		this.units = units;
	}
}