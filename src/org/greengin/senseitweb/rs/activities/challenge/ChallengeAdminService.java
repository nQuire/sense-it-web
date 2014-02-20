package org.greengin.senseitweb.rs.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeStageRequest;

@Path("/project/{projectId}/challenge/admin")
public class ChallengeAdminService {


	@Path("/stage")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project setStage(@PathParam("projectId") Long projectId, ChallengeStageRequest stage, @Context HttpServletRequest request) {
		ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
		return manager.setStage(stage.getStage());
	}
	
	@Path("/answers")
	@GET
	@Produces("application/json")
	@JsonView({Views.VotableCountModeration.class})
	public Collection<ChallengeAnswer> get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ChallengeActivityActions participant = new ChallengeActivityActions(projectId, request);
		return participant.getAnswersForAdmin();
	}
}
