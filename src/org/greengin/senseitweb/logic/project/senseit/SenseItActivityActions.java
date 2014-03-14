package org.greengin.senseitweb.logic.project.senseit;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.entities.activities.senseit.SenseItProfile;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.entities.activities.senseit.SensorInput;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.data.DataActions;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItOperations;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;

public class SenseItActivityActions extends DataActions<SenseItSeries, SenseItAnalysis, SenseItActivity> {

	public SenseItActivityActions(Long projectId, HttpServletRequest request) {
		super(projectId, request, SenseItSeries.class, SenseItAnalysis.class, SenseItActivity.class);
	}
	
	/** member actions **/
	
	@Override
	public Collection<SenseItSeries> getData() {
		SenseItOperations.init(request.getServletContext());
		return super.getData();
	}
	
	public byte[] getPlot(Long dataId, String varId) {
		if (hasMemberAccessIgnoreToken()) {
			SenseItSeries series = em.find(SenseItSeries.class, dataId);
			SenseItProcessedSeriesVariable data = series.varData(varId);
			return SenseItPlots.createPlot("", data);
		} else {
			return null;
		}
	}
	

	/** editor actions **/

	
	public Project updateProfile(SenseItProfileRequest profileData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			profileData.updateProfile(activity.getProfile());
			em.getTransaction().commit();

			return project;
		}
		
		return null;
	}


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
