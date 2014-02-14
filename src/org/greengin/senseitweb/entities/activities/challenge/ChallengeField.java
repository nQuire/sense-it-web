package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.greengin.senseitweb.entities.IdEntity;


@Entity
public class ChallengeField extends IdEntity {
	

	@Basic
	ChallengeFieldType type;
	
	@Basic
	String label;
	

	
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
