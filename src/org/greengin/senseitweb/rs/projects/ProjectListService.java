package org.greengin.senseitweb.rs.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.logic.project.ProjectListManager;
import org.greengin.senseitweb.persistence.EMF;

@Path("/projects")
public class ProjectListService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> projectList(@Context HttpServletRequest request) {
		ProjectListManager manager = new ProjectListManager(request);
		return manager.getProjects();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Long create(ProjectCreationRequest projectData, @Context HttpServletRequest request) {
		ProjectListManager manager = new ProjectListManager(request);
		return manager.createProject(projectData);
	}
	
	@POST
	@Path("/access")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
    public Map<Long, AccessLevel> getAccess(List<Long> projectIds, @Context HttpServletRequest request) {
		HashMap<Long, AccessLevel> levels = new HashMap<Long, AccessLevel>();
		
		UserProfile user = UsersManager.get().currentUser(request);
		EntityManager em = EMF.get().createEntityManager();
		
		for (Long id : projectIds) {
			levels.put(id, SubscriptionManager.get().getAccessLevel(em, user, id));
		}
		
		return levels;
    }
	
}
