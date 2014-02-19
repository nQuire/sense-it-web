package org.greengin.senseitweb.rs.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityManager;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;

@Path("/project/{projectId}/challenge/votes")
public class ChallengeVoteService {

	@GET
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public Collection<ChallengeAnswer> get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ChallengeActivityManager participant = new ChallengeActivityManager(projectId, request);
		Collection<ChallengeAnswer> answers = participant.getAnswersForParticipant();
		for (ChallengeAnswer a : answers) {
			a.selectVoteAuthor(participant.getCurrentUser());
		}
		return answers;
	}
	
	
	@Path("/{answerId}")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public VoteCount vote(@PathParam("projectId") Long projectId, @PathParam("answerId") Long answerId, VoteRequest voteData, @Context HttpServletRequest request) {
		ChallengeActivityManager voter = new ChallengeActivityManager(projectId, request);
		
		return voter.vote(answerId, voteData);
	}
}
