package org.greengin.senseitweb.rs.projects;

import java.util.Collection;

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

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.ProjectResponse;
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
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.deleteProject();
	}

	@Path("/metadata")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, ProjectRequest projectData, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.updateMetadata(projectData);
	}
	
	@Path("/join")
	@POST
	@Produces("application/json")
	public AccessLevel join(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.join();
	}

	@Path("/leave")
	@POST
	@Produces("application/json")
	public AccessLevel leave(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.leave();
	}
	
	@Path("/admin/open")
	@PUT
	@Produces("application/json")
	public Project open(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.setOpen(true);
	}

	@Path("/admin/close")
	@PUT
	@Produces("application/json")
	public Project close(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.setOpen(false);
	}
	
	@Path("/admin/users")
	@GET
	@Produces("application/json")
	@JsonView({Views.User.class})
	public Collection<UserProfile> users(@PathParam("projectId") Long projectId, @Context HttpServletRequest request) {
		ProjectActions manager = new ProjectActions(projectId, request);
		return manager.getUsers();
	}

}
