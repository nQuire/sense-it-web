package org.greengin.senseitweb.rs.activities.challenge;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeOutcomeRequest;


@Path("/project/{projectId}/challenge/outcome")
public class ChallengeOutcomeService {


	@GET
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ChallengeActivityActions participant = new ChallengeActivityActions(projectId, request);
		return participant.getOutcome();
	}

	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome select(@PathParam("projectId") Long projectId, ChallengeOutcomeRequest outcomeData, @Context HttpServletRequest request) {
		ChallengeActivityActions admin = new ChallengeActivityActions(projectId, request);
		return admin.updateOutcome(outcomeData);
	}
	
}
