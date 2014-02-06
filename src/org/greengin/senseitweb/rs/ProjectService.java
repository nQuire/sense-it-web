package org.greengin.senseitweb.rs;


import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.persistence.EMF;

@Path("/project/{projectId}")
public class ProjectService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Project getHtml(@PathParam("projectId") Long projectId) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		return project;
    }
}
