package org.greengin.senseitweb.logic.project.challenge;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.logic.project.AbstractActivityParticipant;

public class ChallengeActivityParticipant extends AbstractActivityParticipant<ChallengeActivity> {

	public ChallengeActivityParticipant(Long projectId, HttpServletRequest request) {
		super(projectId, request, ChallengeActivity.class);
		
		
	}
	
	

}
