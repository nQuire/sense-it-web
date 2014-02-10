package org.greengin.senseitweb.rs.challenge;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.persistence.EMF;

@Path("/project/{projectId}/challenge")
public class ChallengeActivityService {

	@Path("/fields")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") String projectId, ChallengeFieldRequest fieldData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		if (project.getType() == ProjectType.CHALLENGE && project.getActivity() instanceof ChallengeActivity) {
			ChallengeActivity activity = (ChallengeActivity) project.getActivity();

			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.persist(field);
			em.getTransaction().commit();
			
			return em.find(Project.class, projectId);			
		} else {
			em.close();
			return null;
		}
	}

	@Path("/field/{fieldId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") String projectId, @PathParam("fieldId") String fieldId, ChallengeFieldRequest fieldData) {
		EntityManager em = EMF.get().createEntityManager();

		ChallengeField field = em.find(ChallengeField.class, fieldId);

		em.getTransaction().begin();
		fieldData.update(field);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);
		return project;
	}
	
	@Path("/field/{fieldId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") String projectId, @PathParam("fieldId") String fieldId) {
		EntityManager em = EMF.get().createEntityManager();

		ChallengeField field = em.find(ChallengeField.class, fieldId);

		em.getTransaction().begin();
		em.remove(field);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);
		return project;
	}
	
}
