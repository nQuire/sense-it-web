package org.greengin.senseitweb.rs.requestdata;

import org.greengin.senseitweb.entities.senseit.SensorInput;

public class SensorInputData {
	String sensor;
	float rate;
	

	public String getSensor() {
		return sensor;
	}


	public void setSensor(String sensor) {
		this.sensor = sensor;
	}


	public float getRate() {
		return rate;
	}


	public void setRate(float rate) {
		this.rate = rate;
	}


	public void updateInput(SensorInput input) {
		input.setRate(rate);
		input.setSensor(sensor);
	}
}
