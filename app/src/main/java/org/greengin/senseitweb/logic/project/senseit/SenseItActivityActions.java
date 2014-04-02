package org.greengin.senseitweb.logic.project.senseit;

import org.greengin.senseitweb.entities.activities.senseit.*;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.data.DataActions;
import org.greengin.senseitweb.logic.data.FileManagerBean;
import org.greengin.senseitweb.logic.permissions.SubscriptionManagerBean;
import org.greengin.senseitweb.logic.permissions.UsersManagerBean;
import org.greengin.senseitweb.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

public class SenseItActivityActions extends DataActions<SenseItSeries, SenseItAnalysis, SenseItActivity> {


    public SenseItActivityActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, projectId, SenseItActivity.class, SenseItSeries.class, SenseItAnalysis.class, user, tokenOk);
    }

    public SenseItActivityActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, projectId, SenseItActivity.class, SenseItSeries.class, SenseItAnalysis.class, request);
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


    public Project updateProfile(SenseItProfileRequest profileData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            profileData.updateProfile(activity.getProfile());
            em.getTransaction().commit();

            return project;
        }

        return null;
    }


    public Project createSensor(SensorInputRequest inputData) {
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

            return project;
        }

        return null;
    }

    public Project updateSensor(Long inputId, SensorInputRequest inputData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            SensorInput input = em.find(SensorInput.class, inputId);

            em.getTransaction().begin();
            inputData.updateInput(input);
            em.getTransaction().commit();

            return project;
        }

        return null;
    }

    public Project deleteSensor(Long inputId) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
            SensorInput input = em.find(SensorInput.class, inputId);

            em.getTransaction().begin();
            activity.getProfile().getSensorInputs().remove(input);
            em.getTransaction().commit();

            return project;
        }

        return null;
    }


}
