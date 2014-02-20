package org.greengin.senseitweb.entities.activities.challenge;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.greengin.senseitweb.entities.AbstractEntity;

@Entity
public class ChallengeOutcome extends AbstractEntity {
	
	@OneToOne
	ChallengeAnswer selectedAnswer;
	
	@Lob
	@Basic
	String explanation;

	
	public ChallengeAnswer getSelectedAnswer() {
		return selectedAnswer;
	}

	public void setSelectedAnswer(ChallengeAnswer selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}	
	
}
