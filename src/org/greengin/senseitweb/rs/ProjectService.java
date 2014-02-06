package org.greengin.senseitweb.rs;


import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.persistence.EMF;
import org.greengin.senseitweb.rs.requestdata.ProjectMetadata;

@Path("/project/{projectId}")
public class ProjectService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Project getHtml(@PathParam("projectId") String projectId) {
		EntityManager em = EMF.get().createEntityManager();
		return em.find(Project.class, projectId);
    }
	
	@Path("/metadata")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") String projectId, ProjectMetadata projectData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		projectData.updateProject(project);
		em.persist(project);
		em.close();		
		return project;
	}
	
	
}

