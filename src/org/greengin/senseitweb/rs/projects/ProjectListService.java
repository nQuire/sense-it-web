package org.greengin.senseitweb.rs.projects;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.persistence.EMF;

@Path("/projects")
public class ProjectListService {
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Project> projectList() {
		EntityManager em = EMF.get().createEntityManager();
		Query query = em.createQuery(String.format("select from %s", Project.class.getName()));
		return (List<Project>) query.getResultList();
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public String create(ProjectCreationRequest projectData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = new Project();
		projectData.initProject(project);
		
		em.persist(project);
		em.refresh(project);
		return project.getId();
	}
	
}
