package org.greengin.senseitweb.entities.activities.senseit;

import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.greengin.senseitweb.entities.AbstractEntity;


@Entity
public class SenseItProfile extends AbstractEntity {
	

	@OneToMany(orphanRemoval=true, cascade = CascadeType.ALL)
	Collection<SensorInput> sensorInputs = new Vector<SensorInput>();

	public Collection<SensorInput> getSensorInputs() {
		return sensorInputs;
	}

	public void setSensorInputs(Collection<SensorInput> sensorInputs) {
		this.sensorInputs = sensorInputs;
	}
	
}
