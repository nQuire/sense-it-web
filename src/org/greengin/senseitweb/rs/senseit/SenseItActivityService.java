package org.greengin.senseitweb.rs.senseit;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.persistence.EMF;

@Path("/project/{projectId}/senseit")
public class SenseItActivityService {

	@Path("/profiles")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, SenseItProfileRequest profileData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		if (project.getType() == ProjectType.SENSEIT && project.getActivity() instanceof SenseItActivity) {
			SenseItActivity activity = (SenseItActivity) project.getActivity();

			em.getTransaction().begin();
			SenseItProfile profile = new SenseItProfile();
			profileData.updateProfile(profile);
			activity.getProfiles().add(profile);
			em.persist(profile);
			em.getTransaction().commit();
			
			return em.find(Project.class, projectId);			
		} else {
			em.close();
			return null;
		}
	}

	@Path("/profile/{profileId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, SenseItProfileRequest profileData) {
		EntityManager em = EMF.get().createEntityManager();

		SenseItProfile profile = em.find(SenseItProfile.class, profileId);

		em.getTransaction().begin();
		profileData.updateProfile(profile);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);

		return project;
	}
	
	@Path("/profile/{profileId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId) {
		EntityManager em = EMF.get().createEntityManager();

		SenseItProfile profile = em.find(SenseItProfile.class, profileId);

		em.getTransaction().begin();
		em.remove(profile);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);

		return project;
	}
	
	
	@Path("/profile/{profileId}/inputs")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, SensorInputRequest inputData) {
		EntityManager em = EMF.get().createEntityManager();

		SenseItProfile profile = em.find(SenseItProfile.class, profileId);
		SensorInput input = new SensorInput();
		
		em.getTransaction().begin();
		inputData.updateInput(input);
		profile.getSensorInputs().add(input);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);

		return project;
	}

	@Path("/profile/{profileId}/input/{inputId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") Long projectId, @PathParam("profileId") Long profileId, @PathParam("inputId") Long inputId, SensorInputRequest inputData) {
		EntityManager em = EMF.get().createEntityManager();

		SensorInput input = em.find(SensorInput.class, inputId);
		em.getTransaction().begin();
		inputData.updateInput(input);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);
		return project;
	}
	
	@Path("/profile/{profileId}/input/{inputId}")
	@DELETE
	@Produces("application/json")
	public Project delete(@PathParam("projectId") String projectId, @PathParam("profileId") String profileId, @PathParam("inputId") String inputId) {
		EntityManager em = EMF.get().createEntityManager();

		SensorInput input = em.find(SensorInput.class, inputId);

		em.getTransaction().begin();
		em.remove(input);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);

		return project;
	}

}
