package org.greengin.senseitweb.rs.projects;

import javax.persistence.EntityManager;
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
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.ProjectEditor;
import org.greengin.senseitweb.logic.project.ProjectParticipant;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.permissions.AccessLevel;
import org.greengin.senseitweb.permissions.SubscriptionManager;
import org.greengin.senseitweb.persistence.EMF;

@Path("/project/{projectId}")
public class ProjectService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ProjectResponse get(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		AccessLevel access = SubscriptionManager.get().getAccessLevel(project, request);

		ProjectResponse response = new ProjectResponse();
		response.setProject(project);
		response.setAccess(access);
		return response;
	}

	@DELETE
	@Produces("application/json")
	public Boolean delete(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectEditor editor = new ProjectEditor(projectId, request);
		return editor.deleteProject();
	}

	@Path("/metadata")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, ProjectRequest projectData, @Context HttpServletRequest request) {
		ProjectEditor editor = new ProjectEditor(projectId, request);
		return editor.updateMetadata(projectData);
	}
	
	@Path("/join")
	@POST
	@Produces("application/json")
	public AccessLevel join(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectParticipant participant = new ProjectParticipant(projectId, request);
		return participant.join();
	}

	@Path("/leave")
	@POST
	@Produces("application/json")
	public AccessLevel leave(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectParticipant participant = new ProjectParticipant(projectId, request);
		return participant.leave();
	}

}
