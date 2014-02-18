package org.greengin.senseitweb.entities.activities.senseit;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.greengin.senseitweb.entities.AbstractEntity;


@Entity
public class SensorInput extends AbstractEntity {

	@Basic
	float rate;
	
	@Basic
	String sensor;
	

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	
}
