package org.greengin.senseitweb.rs.activities.senseit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItProfileRequest;
import org.greengin.senseitweb.logic.project.senseit.SensorInputRequest;

@Path("/project/{projectId}/senseit")
public class SenseItActivityService {

	
	@Path("/inputs")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, SensorInputRequest inputData, @Context HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.createSensor(inputData);
	}

	@Path("/input/{inputId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("inputId") Long inputId, SensorInputRequest inputData, @Context HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.updateSensor(inputId, inputData);
	}
	
	@Path("/input/{inputId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") Long projectId, @PathParam("inputId") Long inputId, @Context HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.deleteSensor(inputId);
	}

}
