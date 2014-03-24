package org.greengin.senseitweb.logic.project.challenge;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;

public class ChallengeActivityRequest {
	Integer maxAnswers;


	public Integer getMaxAnswers() {
		return maxAnswers;
	}


	public void setMaxAnswers(Integer maxAnswers) {
		this.maxAnswers = maxAnswers;
	}


	public void update(ChallengeActivity activity) {
		activity.setMaxAnswers(maxAnswers);
	}
}
