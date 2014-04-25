package org.greengin.senseitweb.logic.project.challenge;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.project.AbstractActivityActions;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.logic.rating.VoteCount;
import org.greengin.senseitweb.logic.rating.VoteRequest;

public class ChallengeActivityActions extends AbstractActivityActions<ChallengeActivity> {

	private static final String MY_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";

	private static final String ALL_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity";

	private static final String ANSWER_COUNT_QUERY = "SELECT COUNT(an) AS N FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";



    public ChallengeActivityActions(ContextBean context, Long projectId, UserProfile user, boolean tokenOk) {
        super(context, projectId, ChallengeActivity.class, user, tokenOk);
    }

    public ChallengeActivityActions(ContextBean context, Long projectId, HttpServletRequest request) {
        super(context, projectId, ChallengeActivity.class, request);
    }

	/** common actions **/

	private List<ChallengeAnswer> getAnswers(boolean onlyMine, boolean onlyModerated) {
		boolean access = hasAccess(PermissionType.PROJECT_ADMIN)
				|| (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && (onlyMine || activity.getStage() != ChallengeActivityStage.PROPOSAL));

		if (access) {
			TypedQuery<ChallengeAnswer> query = context.createEntityManager().createQuery(onlyMine ? MY_ANSWERS_QUERY : ALL_ANSWERS_QUERY,
                    ChallengeAnswer.class);
			query.setParameter("activity", project.getActivity());
			if (onlyMine) {
				query.setParameter("author", user);
			}
			return query.getResultList();
		} 
		
		return null;
	}

	public Collection<ChallengeAnswer> getAnswersForParticipant() {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
			return getAnswers(activity.getStage() == ChallengeActivityStage.PROPOSAL, true);
		} 
				
		return null;
	}

	public Collection<ChallengeAnswer> getAnswersForAdmin() {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
			return getAnswers(false, false);
		} 
		
		return null;
	}

	/** participant actions **/

	public NewChallengeAnswerResponse createAnswer(ChallengeAnswerRequest answerData) {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            EntityManager em = context.createEntityManager();

			TypedQuery<Long> query = em.createQuery(ANSWER_COUNT_QUERY, Long.class);
			query.setParameter("activity", project.getActivity());
			query.setParameter("author", user);
			Long n = query.getSingleResult();

			if (n < activity.getMaxAnswers()) {
				em.getTransaction().begin();
				ChallengeAnswer answer = new ChallengeAnswer();
				answer.setAuthor(user);
				answerData.update(answer);
                activity.getAnswers().add(answer);
				em.persist(answer);
				em.getTransaction().commit();

				NewChallengeAnswerResponse response = new NewChallengeAnswerResponse();
				response.setNewAnswer(answer.getId());
				response.setAnswers(getAnswersForParticipant());
				
				return response;
			}
		}
		
		return null;
	}

	public Collection<ChallengeAnswer> updateAnswer(Long answerId, ChallengeAnswerRequest answerData) {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            EntityManager em = context.createEntityManager();
			em.getTransaction().begin();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			answerData.update(answer);
			em.getTransaction().commit();
			return getAnswersForParticipant();
		}
		
		return null;
	}

	public Collection<ChallengeAnswer> deleteAnswer(Long answerId) {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
            EntityManager em = context.createEntityManager();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
            if (user.equals(answer.getAuthor())) {
                em.getTransaction().begin();
                if (activity.getOutcome().getSelectedAnswer() == answer) {
                    activity.getOutcome().setSelectedAnswer(null);
                }
                activity.getAnswers().remove(answer);
                em.remove(answer);
                em.getTransaction().commit();
                return getAnswersForParticipant();
            }
		}
		
		return null;
	}

	public VoteCount vote(Long answerId, VoteRequest voteData) {
		if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION) && activity.getStage() == ChallengeActivityStage.VOTING) {
            EntityManager em = context.createEntityManager();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			if (answer != null && activity.getAnswers().contains(answer)) {
                return context.getVoteManager().vote(user, answer, voteData);
			}
		}

		return null;
	}

	/** admin actions **/

	public ProjectResponse setStage(ChallengeActivityStage stage) {
		if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            EntityManager em = context.createEntityManager();
			em.getTransaction().begin();
			activity.setStage(stage);
			em.getTransaction().commit();
			return projectResponse(project);
		}
		
		return null;
	}

	/** editor actions **/

	public ProjectResponse updateActivity(ChallengeActivityRequest activityData) {
		if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
			em.getTransaction().begin();
			activityData.update(activity);
			em.getTransaction().commit();
			return projectResponse(project);
		}

		return null;
	}

	public ProjectResponse createField(ChallengeFieldRequest fieldData) {
		if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.getTransaction().commit();
			return projectResponse(project);
		}
		
		return null;
	}

	public ProjectResponse updateField(Long fieldId, ChallengeFieldRequest fieldData) {
		if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			fieldData.update(field);
			em.getTransaction().commit();
			
			return projectResponse(project);
		}
		
		return null;
	}

	public ProjectResponse deleteField(Long fieldId) {
		if (hasAccess(PermissionType.PROJECT_EDITION)) {
            EntityManager em = context.createEntityManager();
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			activity.getFields().remove(field);
			em.getTransaction().commit();
		}
		return projectResponse(project);
	}

    /** outcome actions **/

	public ChallengeOutcome updateOutcome(ChallengeOutcomeRequest outcomeData) {
		if (hasAccess(PermissionType.PROJECT_ADMIN)) {
            EntityManager em = context.createEntityManager();
			em.getTransaction().begin();
			outcomeData.update(em, activity.getOutcome());
			em.getTransaction().commit();
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

}
