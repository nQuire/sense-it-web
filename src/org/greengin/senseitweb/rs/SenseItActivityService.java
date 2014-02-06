package org.greengin.senseitweb.rs;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.greengin.senseitweb.entities.Project;
import org.greengin.senseitweb.entities.ProjectType;
import org.greengin.senseitweb.entities.senseit.Profile;
import org.greengin.senseitweb.entities.senseit.SenseItActivity;
import org.greengin.senseitweb.persistence.EMF;
import org.greengin.senseitweb.rs.requestdata.ProfileMetadata;

@Path("/project/{projectId}/senseit")
public class SenseItActivityService {

	@Path("/profiles")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Project create(@PathParam("projectId") String projectId, ProfileMetadata profileData) {
		EntityManager em = EMF.get().createEntityManager();
		Project project = em.find(Project.class, projectId);
		if (project.getType() == ProjectType.SENSEIT && project.getActivity() instanceof SenseItActivity) {
			SenseItActivity activity = (SenseItActivity) project.getActivity();

			Profile profile = new Profile();
			profileData.updateProfile(profile);

			activity.getProfiles().add(profile);
			em.persist(activity);

			em.close();
			return project;
		} else {
			return null;
		}
	}

	@Path("/profile/{profileId}")
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Project update(@PathParam("projectId") String projectId, @PathParam("profileId") String profileId, ProfileMetadata profileData) {
		EntityManager em = EMF.get().createEntityManager();

		Profile profile = em.find(Profile.class, profileId);

		em.getTransaction().begin();
		profileData.updateProfile(profile);
		em.getTransaction().commit();

		Project project = em.find(Project.class, projectId);

		return project;
	}

}
