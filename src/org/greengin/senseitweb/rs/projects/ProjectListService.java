package org.greengin.senseitweb.rs.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
import org.greengin.senseitweb.logic.permissions.PermissionsManager;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.persistence.EMF;

@Path("/projects")
public class ProjectListService {
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> projectList() {
		EntityManager em = EMF.get().createEntityManager();
		Query query = em.createQuery(String.format("SELECT p FROM %s p", Project.class.getName()));
		return (List<Project>) query.getResultList();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Long create(ProjectCreationRequest projectData, @Context HttpServletRequest request) {
		UserProfile user = UsersManager.get().currentUser(request);
		
		if (PermissionsManager.get().canCreateProject(user)) {
		
			EntityManager em = EMF.get().createEntityManager();
			
			em.getTransaction().begin();
			Project project = new Project();
			projectData.initProject(project);
			em.persist(project);
			SubscriptionManager.get().projectCreatedInTransaction(em, project, user);
			em.getTransaction().commit();
		
			return project.getId();
		}
		
		return null;		
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
