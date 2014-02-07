package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.datanucleus.jpa.annotations.Extension;

@Entity
public class ChallengeField {
	

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	String id;
	
	@Basic
	ChallengeFieldType type;
	
	@Basic
	String label;
	

	public String getId() {
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
