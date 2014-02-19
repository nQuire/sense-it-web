package org.greengin.senseitweb.logic.project.senseit;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.project.AbstractActivityManager;

public class SenseItActivityManager extends AbstractActivityManager<SenseItActivity> {

	public SenseItActivityManager(Long projectId, HttpServletRequest request) {
		super(projectId, request, SenseItActivity.class);
	}

	/** editor actions **/

	public Project createProfile(SenseItProfileRequest profileData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			SenseItProfile profile = new SenseItProfile();
			profileData.updateProfile(profile);
			activity.getProfiles().add(profile);
			em.persist(profile);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project updateProfile(Long profileId, SenseItProfileRequest profileData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);

			em.getTransaction().begin();
			profileData.updateProfile(profile);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project deleteProfile(Long profileId) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);

			em.getTransaction().begin();
			activity.getProfiles().remove(profile);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project createSensor(Long profileId, SensorInputRequest inputData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);

			em.getTransaction().begin();
			SensorInput input = new SensorInput();
			inputData.updateInput(input);
			profile.getSensorInputs().add(input);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project updateSensor(Long profileId, Long inputId, SensorInputRequest inputData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SensorInput input = em.find(SensorInput.class, inputId);

			em.getTransaction().begin();
			inputData.updateInput(input);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project deleteSensor(Long profileId, Long inputId) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);
			SensorInput input = em.find(SensorInput.class, inputId);

			em.getTransaction().begin();
			profile.getSensorInputs().remove(input);
			em.getTransaction().commit();
		}
		return project;
	}

}
