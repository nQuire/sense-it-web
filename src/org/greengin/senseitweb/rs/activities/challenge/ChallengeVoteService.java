package org.greengin.senseitweb.rs.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityParticipant;

@Path("/project/{projectId}/challenge/voting")
public class ChallengeVoteService {

	@GET
	@Produces("application/json")
	@JsonView({Views.VotableIncludeAnonymousVotes.class})
	public Collection<ChallengeAnswer> get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ChallengeActivityParticipant participant = new ChallengeActivityParticipant(projectId, request);
		return participant.getAllAnswers();
	}
}
