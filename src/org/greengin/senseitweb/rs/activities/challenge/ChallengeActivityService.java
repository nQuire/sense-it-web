package org.greengin.senseitweb.rs.activities.challenge;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityEditor;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeFieldRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeStageRequest;

@Path("/project/{projectId}/challenge")
public class ChallengeActivityService {

	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project updateActivity(@PathParam("projectId") Long projectId, ChallengeActivityRequest activityData, @Context HttpServletRequest request) {
		ChallengeActivityEditor editor = new ChallengeActivityEditor(projectId, request);
		return editor.updateActivity(activityData);
	}

	@Path("/fields")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, ChallengeFieldRequest fieldData, @Context HttpServletRequest request) {
		ChallengeActivityEditor editor = new ChallengeActivityEditor(projectId, request);
		return editor.createField(fieldData);
	}

	@Path("/field/{fieldId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("fieldId") Long fieldId, ChallengeFieldRequest fieldData, @Context HttpServletRequest request) {
		ChallengeActivityEditor editor = new ChallengeActivityEditor(projectId, request);
		return editor.updateField(fieldId, fieldData);
	}
	
	@Path("/field/{fieldId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") Long projectId, @PathParam("fieldId") Long fieldId, @Context HttpServletRequest request) {
		ChallengeActivityEditor editor = new ChallengeActivityEditor(projectId, request);
		return editor.deleteField(fieldId);
	}
	
	@Path("/stage")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project setStage(@PathParam("projectId") Long projectId, ChallengeStageRequest stage, @Context HttpServletRequest request) {
		ChallengeActivityEditor editor = new ChallengeActivityEditor(projectId, request);
		return editor.setStage(stage.getStage());
	}
	
}
