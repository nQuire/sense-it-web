package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Entity;

import org.greengin.senseitweb.entities.AbstractEntity;


@Entity
public class ChallengeField extends AbstractEntity {
	

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
