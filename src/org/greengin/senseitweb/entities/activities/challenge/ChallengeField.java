package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ChallengeField {
	

	@Id
	@Column(name = "FIELD_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Basic
	ChallengeFieldType type;
	
	@Basic
	String label;
	

	public Long getId() {
		return id;
	}
	
	public ChallengeFieldType getType() {
		return type;
	}

	public void setType(ChallengeFieldType type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
