package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.*;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.data.DataActions;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class SenseItActivityActions extends DataActions<SenseItSeries, SenseItAnalysis, SenseItActivity> {


    public SenseItActivityActions(Long projectId, SubscriptionManager subscriptionManager, FileManager fileManager, UserProfile user, boolean tokenOk, EntityManager em) {
        super(projectId, SenseItActivity.class, SenseItSeries.class, SenseItAnalysis.class, subscriptionManager, fileManager, user, tokenOk, em);
    }

    public SenseItActivityActions(Long projectId, SubscriptionManager subscriptionManager, FileManager fileManager, UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(projectId, SenseItActivity.class, SenseItSeries.class, SenseItAnalysis.class, subscriptionManager, fileManager, usersManager, em, request);
    }


    /**
     * member actions *
     */

    public byte[] getPlot(Long dataId, String varId) {
        if (hasMemberAccessIgnoreToken()) {
            SenseItSeries series = em.find(SenseItSeries.class, dataId);
            SenseItProcessedSeriesVariable data = series.varData(varId);
            return SenseItPlots.createPlot(data);
        } else {
            return null;
        }
    }


    /**
     * editor actions *
     */


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
