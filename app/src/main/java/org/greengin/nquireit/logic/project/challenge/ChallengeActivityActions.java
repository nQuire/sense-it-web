package org.greengin.nquireit.logic.project.challenge;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.greengin.nquireit.entities.activities.challenge.ChallengeActivity;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.nquireit.entities.activities.challenge.ChallengeAnswer;
import org.greengin.nquireit.entities.activities.challenge.ChallengeOutcome;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.activity.AbstractActivityActions;
import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;

public class ChallengeActivityActions extends AbstractActivityActions<ChallengeActivity> {


    public ChallengeActivityActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, projectId, ChallengeActivity.class, user, tokenOk);
    }

    public ChallengeActivityActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, projectId, ChallengeActivity.class, request);
    }

    /**
     * common actions *
     */

    private List<ChallengeAnswer> getAnswers(boolean onlyMine, boolean onlyModerated) {

        if (hasAccess(PermissionType.PROJECT_ADMIN)
                || (hasAccess(PermissionType.PROJECT_BROWSE) && (onlyMine || activity.getStage() != ChallengeActivityStage.PROPOSAL))) {
            return context.getChallengeDao().getAnswers(onlyMine, project.getActivity(), user);
        }

        return null;
    }

    public Collection<ChallengeAnswer> getAnswersForParticipant() {
        if (hasAccess(PermissionType.PROJECT_BROWSE)) {
            return getAnswers(activity.getStage() == ChallengeActivityStage.PROPOSAL, true);
        }

        return null;
    }

    public Collection<ChallengeAnswer> getAnswersForAdmin() {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            return getAnswers(false, false);
        }

        return null;
    }

    /**
     * participant actions *
     */


    public NewChallengeAnswerResponse createAnswer(ChallengeAnswerRequest answerData) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {

            long n = context.getChallengeDao().answerCount(project.getActivity(), user);

            if (n < activity.getMaxAnswers()) {
                long newAnswerId = context.getChallengeDao().createAnswer(activity, user, answerData);
                NewChallengeAnswerResponse response = new NewChallengeAnswerResponse();
                response.setNewAnswer(newAnswerId);
                response.setAnswers(getAnswersForParticipant());

                context.getProjectDao().updateActivityTimestamp(project);

                return response;
            }
        }

        return null;
    }

    public Collection<ChallengeAnswer> updateAnswer(Long answerId, ChallengeAnswerRequest answerData) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            context.getChallengeDao().updateAnswer(activity, user, answerId, answerData);
            context.getProjectDao().updateActivityTimestamp(project);
            return getAnswersForParticipant();
        }

        return null;
    }

    public Collection<ChallengeAnswer> deleteAnswer(Long answerId) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            context.getChallengeDao().deleteAnswer(activity, user, answerId);
            context.getProjectDao().updateActivityTimestamp(project);
            return getAnswersForParticipant();
        }

        return null;
    }

    public VoteCount vote(Long answerId, VoteRequest voteData) {
        if ((hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.VOTING) ||
                (voteData.isReport() &&
                        (hasAccess(PermissionType.PROJECT_ADMIN) ||
                                (loggedWithToken && activity.getStage() != ChallengeActivityStage.PROPOSAL)))) {
            ChallengeAnswer answer = context.getChallengeDao().getAnswer(activity, answerId);
            if (answer != null) {
                return context.getVoteDao().vote(user, answer, voteData);
            }
        }

        return null;
    }

    /**
     * admin actions *
     */
    public ProjectResponse setStage(ChallengeActivityStage stage) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            context.getChallengeDao().setActivityStage(activity, stage);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }

        return null;
    }

    /**
     * editor actions *
     */
    public ProjectResponse updateActivity(ChallengeActivityRequest activityData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getChallengeDao().updateActivity(activity, activityData);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }

        return null;
    }

    public ProjectResponse createField(ChallengeFieldRequest fieldData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getChallengeDao().createActivityField(activity, fieldData);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }

        return null;
    }

    public ProjectResponse updateField(Long fieldId, ChallengeFieldRequest fieldData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getChallengeDao().updateActivityField(activity, fieldId, fieldData);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }

        return null;
    }

    public ProjectResponse deleteField(Long fieldId) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getChallengeDao().deleteActivityField(activity, fieldId);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }
        return null;
    }

    /**
     * outcome actions *
     */

    public ChallengeOutcome updateOutcome(ChallengeOutcomeRequest outcomeData) {
        if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            context.getChallengeDao().updateActivityOutcome(activity, outcomeData);
            context.getProjectDao().updateActivityTimestamp(project);
            return activity.getOutcome();
        }

        return null;
    }

    public ChallengeOutcome getOutcome() {
        if (hasAccess(PermissionType.PROJECT_ADMIN)
                || (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.OUTCOME)) {
            return activity.getOutcome();
        }

        return null;
    }

    public ProjectResponse moveField(Long fieldId, ChallengeFieldMoveRequest fieldData) {
        if (hasAccess(PermissionType.PROJECT_EDITION)) {
            context.getChallengeDao().moveActivityField(activity, fieldId, fieldData);
            context.getProjectDao().updateActivityTimestamp(project);
            return projectResponse(project);
        }

        return null;
    }

    public Collection<ChallengeAnswer> submitAnswer(Long answerId, boolean published) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            context.getChallengeDao().submitAnswer(activity, user, answerId, published);
            context.getProjectDao().updateActivityTimestamp(project);
            return getAnswersForParticipant();
        }

        return null;
    }

}
