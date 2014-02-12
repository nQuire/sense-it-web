package org.greengin.senseitweb.logic.projects.challenge;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeFieldType;

public class ChallengeFieldRequest {
	ChallengeFieldType type;
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

	public void update(ChallengeField field) {
		field.setType(type);
		field.setLabel(label);
	}
}
