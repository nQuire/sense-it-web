package org.greengin.nquireit.logic.project.senseit;

import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.senseit.*;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.DataActions;
import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class SenseItActivityActions extends DataActions<SenseItSeries, BaseAnalysis, SenseItActivity> {


    public SenseItActivityActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, projectId, SenseItActivity.class, SenseItSeries.class, BaseAnalysis.class, user, tokenOk);
    }

    public SenseItActivityActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, projectId, SenseItActivity.class, SenseItSeries.class, BaseAnalysis.class, request);
    }


    /**
     * member actions *
     */

    public byte[] getPlot(Long dataId, String varId) {
        if (hasAccess(PermissionType.PROJECT_VIEW_IMAGE)) {
            EntityManager em = context.createEntityManager();
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


    public ProjectResponse updateProfile(SenseItProfileRequest profileData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            profileData.updateProfile(activity.getProfile());
            em.getTransaction().commit();

            return projectResponse(project);
        }

        return null;
    }


    public ProjectResponse createSensor(SensorInputRequest inputData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            SensorInput input = new SensorInput();
            inputData.updateInput(input);
            if (activity.getProfile() == null) {
                activity.setProfile(new SenseItProfile());
            }

            activity.getProfile().getSensorInputs().add(input);
            em.getTransaction().commit();

            return projectResponse(project);
        }

        return null;
    }

    public ProjectResponse updateSensor(Long inputId, SensorInputRequest inputData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            SensorInput input = em.find(SensorInput.class, inputId);

            em.getTransaction().begin();
            inputData.updateInput(input);
            em.getTransaction().commit();

            return projectResponse(project);
        }

        return null;
    }

    public ProjectResponse deleteSensor(Long inputId) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            SensorInput input = em.find(SensorInput.class, inputId);

            em.getTransaction().begin();
            activity.getProfile().getSensorInputs().remove(input);
            em.getTransaction().commit();

            return projectResponse(project);
        }

        return null;
    }

}
