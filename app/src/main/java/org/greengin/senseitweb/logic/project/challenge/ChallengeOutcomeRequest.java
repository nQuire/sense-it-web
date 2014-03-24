package org.greengin.senseitweb.logic.project.challenge;


import javax.persistence.EntityManager;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;

public class ChallengeOutcomeRequest {
	Long answerId;
	String explanation;

	public ChallengeOutcomeRequest() {
		answerId = null;
		explanation = null;
	}

	
	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	

	
	
	public void update(EntityManager em, ChallengeOutcome outcome) {
		if (answerId != null) {
			outcome.setSelectedAnswer(em.find(ChallengeAnswer.class, answerId));
		}
		if (explanation != null) {
			outcome.setExplanation(explanation);
		}
	}
}
