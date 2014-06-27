package org.greengin.nquireit.logic.project.senseit;

import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.entities.activities.senseit.*;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.DataActions;
import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.project.senseit.transformations.SenseItProcessedSeriesVariable;
import org.springframework.transaction.annotation.Transactional;

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
            SenseItSeries series = context.getSenseItDao().getSeries(dataId);
            SenseItProcessedSeriesVariable data = series.varData(varId);
            return SenseItPlots.createPlot(data);
        } else {
            return null;
        }
    }


    /**
     * editor actions *
     */
    @Transactional
    public ProjectResponse updateProfile(SenseItProfileRequest profileData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getSenseItDao().updateProfile(profileData, activity);
            return projectResponse(project);
        }

        return null;
    }

    @Transactional
    public ProjectResponse createSensor(SensorInputRequest inputData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getSenseItDao().createSensor(inputData, activity);
            return projectResponse(project);
        }

        return null;
    }

    @Transactional
    public ProjectResponse updateSensor(Long inputId, SensorInputRequest inputData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getSenseItDao().updateSensor(inputData, inputId);
            return projectResponse(project);
        }

        return null;
    }

    @Transactional
    public ProjectResponse deleteSensor(Long inputId) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getSenseItDao().deleteSensor(activity, inputId);
            return projectResponse(project);
        }

        return null;
    }

}
