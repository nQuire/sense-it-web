package org.greengin.senseitweb.logic.project.challenge;

import java.util.HashMap;
import java.util.Map;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;

public class ChallengeAnswerRequest {
	Map<Long, String> fieldValues = new HashMap<Long, String>();
	Boolean published;
	
	public Map<Long, String> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(Map<Long, String> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	
	public Boolean getPublished() {
		return published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	public void update(ChallengeAnswer answer) {
		answer.setFieldValues(fieldValues);
		answer.setPublished(published);
	}
}
