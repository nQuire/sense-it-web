package org.greengin.senseitweb.logic.projects.senseit;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.logic.projects.ProjectEditor;

public class SenseItActivityEditor extends ProjectEditor {

	SenseItActivity activity;
	
	public SenseItActivityEditor(Long projectId, HttpServletRequest request) {
		super(projectId, request);
		
		if (project.getType() == ProjectType.SENSEIT && project.getActivity() instanceof SenseItActivity) {
			activity = (SenseItActivity) project.getActivity();
		} else {
			hasAccess = false;
		}
	}
	
	
	public Project createProfile(SenseItProfileRequest profileData) {
		if (hasAccess) {
			em.getTransaction().begin();
			SenseItProfile profile = new SenseItProfile();
			profileData.updateProfile(profile);
			activity.getProfiles().add(profile);
			em.persist(profile);
			em.getTransaction().commit();
			
			return project;			
		} else {
			return null;
		}
	}
	
	public Project updateProfile(Long profileId, SenseItProfileRequest profileData) {
		if (hasAccess) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);

			em.getTransaction().begin();
			profileData.updateProfile(profile);
			em.getTransaction().commit();

			return project;
		} else {
			return null;
		}			
	}
	
	public Project deleteProfile(Long profileId) {
		if (hasAccess) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);

			em.getTransaction().begin();
			activity.getProfiles().remove(profile);
			em.getTransaction().commit();
			
			return project;			
		} else {
			return null;
		}
	}
	
	public Project createSensor(Long profileId, SensorInputRequest inputData) {
		if (hasAccess) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);
			
			em.getTransaction().begin();
			SensorInput input = new SensorInput();
			inputData.updateInput(input);
			profile.getSensorInputs().add(input);
			em.getTransaction().commit();
			
			return project;			
		} else {
			return null;
		}
	}
	
	public Project updateSensor(Long profileId, Long inputId, SensorInputRequest inputData) {
		if (hasAccess) {
			SensorInput input = em.find(SensorInput.class, inputId);
			
			em.getTransaction().begin();
			inputData.updateInput(input);
			em.getTransaction().commit();

			return project;
		} else {
			return null;
		}			
	}
	
	public Project deleteSensor(Long profileId, Long inputId) {
		if (hasAccess) {
			SenseItProfile profile = em.find(SenseItProfile.class, profileId);
			SensorInput input = em.find(SensorInput.class, inputId);

			em.getTransaction().begin();
			profile.getSensorInputs().remove(input);
			em.getTransaction().commit();
			
			return project;			
		} else {
			return null;
		}
	}
	
}
