package org.greengin.senseitweb.logic.project.senseit;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.project.AbstractActivityActions;

public class SenseItActivityActions extends AbstractActivityActions<SenseItActivity> {

	public SenseItActivityActions(Long projectId, HttpServletRequest request) {
		super(projectId, request, SenseItActivity.class);
	}

	/** editor actions **/


	public Project createSensor(SensorInputRequest inputData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			
			em.getTransaction().begin();
			SensorInput input = new SensorInput();
			inputData.updateInput(input);
			if (activity.getProfile() == null) {
				activity.setProfile(new SenseItProfile());
			}
			
			activity.getProfile().getSensorInputs().add(input);
			em.getTransaction().commit();

			return project;
		}
		
		return null;
	}

	public Project updateSensor(Long inputId, SensorInputRequest inputData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SensorInput input = em.find(SensorInput.class, inputId);

			em.getTransaction().begin();
			inputData.updateInput(input);
			em.getTransaction().commit();

			return project;
		}
		
		return null;
	}

	public Project deleteSensor(Long inputId) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			SensorInput input = em.find(SensorInput.class, inputId);

			em.getTransaction().begin();
			activity.getProfile().getSensorInputs().remove(input);
			em.getTransaction().commit();

			return project;
		}
		
		return null;
	}

}
