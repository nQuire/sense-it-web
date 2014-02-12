package org.greengin.senseitweb.entities.activities.senseit;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class SenseItProfile {

	@Id
	@Column(name = "PROFILE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Basic
	String title;
	

	@OneToMany(orphanRemoval=true, cascade = CascadeType.ALL)
	Collection<SensorInput> sensorInputs;

	public Long getId() {
		return id;
	}

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
