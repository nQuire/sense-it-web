package org.greengin.senseitweb.rs;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.persistence.EMF;
import org.greengin.senseitweb.rs.requestdata.ProjectMetadata;

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
	public Long create(ProjectMetadata projectMetadata) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = new Project();
		projectMetadata.updateProject(project);
		em.persist(project);
		em.close();		
		return project.getId();
	}
	
}
