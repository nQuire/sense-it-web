package org.greengin.senseitweb.entities.activities.senseit;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.IdEntity;


@Entity
public class SenseItProfile extends IdEntity {

	@Basic
	String title;
	

	@OneToMany(orphanRemoval=true, cascade = CascadeType.ALL)
	Collection<SensorInput> sensorInputs;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<SensorInput> getSensorInputs() {
		return sensorInputs;
	}

	public void setSensorInputs(Collection<SensorInput> sensorInputs) {
		this.sensorInputs = sensorInputs;
	}
	
}
