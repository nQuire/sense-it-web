package org.greengin.senseitweb.logic.project.challenge;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.voting.Voter;


public class ChallengeActivityVoter extends Voter<ChallengeAnswer> {

	
	public ChallengeActivityVoter(Long projectId, Long targetId, HttpServletRequest request) {
		super(request, targetId, ChallengeAnswer.class);
		if (hasAccess) {
			AccessLevel projectAccessLevel = SubscriptionManager.get().getAccessLevel(em, user, projectId);
			hasAccess = projectAccessLevel.isMember();
		}
	}
	
	
}
