package org.greengin.senseitweb.rs.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityParticipant;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.senseitweb.logic.project.challenge.NewChallengeAnswerResponse;

@Path("/project/{projectId}/challenge/answers")
public class ChallengeAnswerService {


	@GET
	@Produces("application/json")
	@JsonView({Views.User.class})
	public Collection<ChallengeAnswer> get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ChallengeActivityParticipant participant = new ChallengeActivityParticipant(projectId, request);
		return participant.getMyAnswers();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public NewChallengeAnswerResponse create(@PathParam("projectId") Long projectId, ChallengeAnswerRequest answerData, @Context HttpServletRequest request) {
		ChallengeActivityParticipant participant = new ChallengeActivityParticipant(projectId, request);
		return participant.createAnswer(answerData);
	}
	
	@Path("/{answerId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Collection<ChallengeAnswer> update(@PathParam("projectId") Long projectId, @PathParam("answerId") Long answerId, ChallengeAnswerRequest answerData, @Context HttpServletRequest request) {
		ChallengeActivityParticipant participant = new ChallengeActivityParticipant(projectId, request);
		return participant.updateAnswer(answerId, answerData);
	}

	@Path("/{answerId}")
	@DELETE
	@Produces("application/json")
	public Collection<ChallengeAnswer> delete(@PathParam("projectId") Long projectId, @PathParam("answerId") Long answerId, @Context HttpServletRequest request) {
		ChallengeActivityParticipant participant = new ChallengeActivityParticipant(projectId, request);
		return participant.deleteAnswer(answerId);
	}

}
