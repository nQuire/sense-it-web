package org.greengin.senseitweb.rs.projects;


import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.persistence.EMF;

@Path("/project/{projectId}")
public class ProjectService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Project get(@PathParam("projectId") Long projectId) {
		EntityManager em = EMF.get().createEntityManager();
		return em.find(Project.class, projectId);
    }
	
	@DELETE
	@Produces("application/json")
	public Boolean delete(@PathParam("projectId") Long projectId) {
		
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		
		em.getTransaction().begin();
		em.remove(project);
		em.getTransaction().commit();
		
		return true;
	}
	
	@Path("/metadata")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, ProjectRequest projectData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		
		em.getTransaction().begin();
		projectData.updateProject(project);
		em.getTransaction().commit();
		
		return em.find(Project.class, projectId);
	}
	
	
}

