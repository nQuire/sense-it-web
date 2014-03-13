package org.greengin.senseitweb.logic.project.senseit.transformations;

import java.util.HashMap;

public class SenseItData {
	HashMap<String, SenseItDataSensor> sensorTypes;
	HashMap<String, String> dataTypes;
	HashMap<String, SenseItDataTransformation> transformations;
	
	public SenseItData() {
		this.sensorTypes = new HashMap<String, SenseItDataSensor>();
		this.dataTypes = new HashMap<String, String>();
		this.transformations = new HashMap<String, SenseItDataTransformation>();
	}

	public HashMap<String, SenseItDataSensor> getSensorTypes() {
		return sensorTypes;
	}

	public void setSensorTypes(HashMap<String, SenseItDataSensor> sensorTypes) {
		this.sensorTypes = sensorTypes;
	}

	public HashMap<String, String> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(HashMap<String, String> dataTypes) {
		this.dataTypes = dataTypes;
	}

	public HashMap<String, SenseItDataTransformation> getTransformations() {
		return transformations;
	}

	public void setTransformations(HashMap<String, SenseItDataTransformation> transformations) {
		this.transformations = transformations;
	}	
	
	
	
	

}
