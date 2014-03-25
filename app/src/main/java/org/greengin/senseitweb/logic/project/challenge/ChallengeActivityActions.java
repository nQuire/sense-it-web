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
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.AbstractActivityActions;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteManager;
import org.greengin.senseitweb.logic.voting.VoteRequest;

public class ChallengeActivityActions extends AbstractActivityActions<ChallengeActivity> {

	private static final String MY_ANSWERS_QUERY = String.format(
			"SELECT a FROM %s a WHERE a.project = :project AND a.author = :author", ChallengeAnswer.class.getName());

	private static final String ALL_ANSWERS_QUERY = String.format("SELECT a FROM %s a WHERE a.project = :project",
			ChallengeAnswer.class.getName());

	private static final String ANSWER_COUNT_QUERY = String.format(
			"SELECT COUNT(a) AS N FROM %s a WHERE a.project = :project AND a.author = :author",
			ChallengeAnswer.class.getName());



    public ChallengeActivityActions(Long projectId, SubscriptionManager subscriptionManager, UserProfile user, boolean tokenOk, EntityManager em) {
        super(projectId, ChallengeActivity.class, subscriptionManager, user, tokenOk, em);
    }

    public ChallengeActivityActions(Long projectId, SubscriptionManager subscriptionManager, UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(projectId, ChallengeActivity.class, subscriptionManager, usersManager, em, request);
    }

	/** common actions **/

	private List<ChallengeAnswer> getAnswers(boolean onlyMine, boolean onlyModerated) {
		boolean access = hasAccess(Role.PROJECT_ADMIN)
				|| (hasAccess(Role.PROJECT_MEMBER) && (onlyMine || activity.getStage() != ChallengeActivityStage.PROPOSAL));

		if (access) {
			TypedQuery<ChallengeAnswer> query = em.createQuery(onlyMine ? MY_ANSWERS_QUERY : ALL_ANSWERS_QUERY,
					ChallengeAnswer.class);
			query.setParameter("project", project);
			if (onlyMine) {
				query.setParameter("author", user);
			}
			return query.getResultList();
		} 
		
		return null;
	}

	public Collection<ChallengeAnswer> getAnswersForParticipant() {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			return getAnswers(activity.getStage() == ChallengeActivityStage.PROPOSAL, true);
		} 
				
		return null;
	}

	public Collection<ChallengeAnswer> getAnswersForAdmin() {
		if (hasAccess(Role.PROJECT_ADMIN)) {
			return getAnswers(false, false);
		} 
		
		return null;
	}

	/** participant actions **/

	public NewChallengeAnswerResponse createAnswer(ChallengeAnswerRequest answerData) {
		if (hasAccess(Role.PROJECT_MEMBER) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
			TypedQuery<Long> query = em.createQuery(ANSWER_COUNT_QUERY, Long.class);
			query.setParameter("project", project);
			query.setParameter("author", user);
			Long n = query.getSingleResult();

			if (n < activity.getMaxAnswers()) {
				em.getTransaction().begin();
				ChallengeAnswer answer = new ChallengeAnswer();
				answer.setAuthor(user);
				answer.setProject(project);
				answerData.update(answer);
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
		if (hasAccess(Role.PROJECT_MEMBER) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
			em.getTransaction().begin();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			answerData.update(answer);
			em.getTransaction().commit();
			return getAnswersForParticipant();
		}
		
		return null;
	}

	public Collection<ChallengeAnswer> deleteAnswer(Long answerId) {
		if (hasAccess(Role.PROJECT_MEMBER) && activity.getStage() == ChallengeActivityStage.PROPOSAL) {
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			em.getTransaction().begin();
			if (activity.getOutcome().getSelectedAnswer() == answer) {
				activity.getOutcome().setSelectedAnswer(null);
			}
			em.remove(answer);
			em.getTransaction().commit();
			return getAnswersForParticipant();
		}
		
		return null;
	}

	public VoteCount vote(Long answerId, VoteRequest voteData) {
		if (hasAccess(Role.PROJECT_MEMBER) && activity.getStage() == ChallengeActivityStage.VOTING) {
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			if (answer != null && answer.getProject() == project) {
				VoteManager voter = new VoteManager(em, user, answer);
				return voter.vote(voteData);
			}
		}

		return null;
	}

	/** admin actions **/

	public Project setStage(ChallengeActivityStage stage) {
		if (hasAccess(Role.PROJECT_ADMIN)) {
			em.getTransaction().begin();
			activity.setStage(stage);
			em.getTransaction().commit();
			return project;
		}
		
		return null;
	}

	/** editor actions **/

	public Project updateActivity(ChallengeActivityRequest activityData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			activityData.update(activity);
			em.getTransaction().commit();
			return project;
		}

		return null;
	}

	public Project createField(ChallengeFieldRequest fieldData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.getTransaction().commit();
			return project;
		}
		
		return null;
	}

	public Project updateField(Long fieldId, ChallengeFieldRequest fieldData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			fieldData.update(field);
			em.getTransaction().commit();
			
			return project;
		}
		
		return null;
	}

	public Project deleteField(Long fieldId) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			activity.getFields().remove(field);
			em.getTransaction().commit();
		}
		return project;
	}

	public ChallengeOutcome updateOutcome(ChallengeOutcomeRequest outcomeData) {
		if (hasAccess(Role.PROJECT_ADMIN)) {
			em.getTransaction().begin();
			outcomeData.update(em, activity.getOutcome());
			em.getTransaction().commit();
			return activity.getOutcome();
		}
		
		return null;
	}

	public ChallengeOutcome getOutcome() {
		if (hasAccess(Role.PROJECT_ADMIN)
				|| (hasAccess(Role.PROJECT_MEMBER) && activity.getStage() == ChallengeActivityStage.OUTCOME)) {
			return activity.getOutcome();
		} 
		
		return null;
	}

}
