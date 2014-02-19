package org.greengin.senseitweb.logic.project.challenge;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeField;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.permissions.Role;
import org.greengin.senseitweb.logic.project.AbstractActivityManager;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteManager;
import org.greengin.senseitweb.logic.voting.VoteRequest;

public class ChallengeActivityManager extends AbstractActivityManager<ChallengeActivity> {

	private static final String MY_ANSWERS_QUERY = String.format(
			"SELECT a FROM %s a WHERE a.project = :project AND a.author = :author", ChallengeAnswer.class.getName());

	private static final String ALL_ANSWERS_QUERY = String.format("SELECT a FROM %s a WHERE a.project = :project",
			ChallengeAnswer.class.getName());

	private static final String ANSWER_COUNT_QUERY = String.format(
			"SELECT COUNT(a) AS N FROM %s a WHERE a.project = :project AND a.author = :author",
			ChallengeAnswer.class.getName());

	public ChallengeActivityManager(Long projectId, HttpServletRequest request) {
		super(projectId, request, ChallengeActivity.class);
	}

	/** common actions **/

	private List<ChallengeAnswer> getAnswers(boolean onlyMine) {
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
		} else {
			return new Vector<ChallengeAnswer>();
		}
	}

	public Collection<ChallengeAnswer> getMyAnswers() {
		return getAnswers(true);
	}

	public Collection<ChallengeAnswer> getAllAnswers() {
		return getAnswers(false);
	}

	/** participant actions **/

	public NewChallengeAnswerResponse createAnswer(ChallengeAnswerRequest answerData) {
		NewChallengeAnswerResponse response = new NewChallengeAnswerResponse();
		response.setNewAnswer(-1l);

		if (hasAccess(Role.PROJECT_MEMBER)) {
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

				response.setNewAnswer(answer.getId());
			}
		}

		response.setAnswers(getMyAnswers());
		return response;
	}

	public Collection<ChallengeAnswer> updateAnswer(Long answerId, ChallengeAnswerRequest answerData) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			em.getTransaction().begin();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			answerData.update(answer);
			em.getTransaction().commit();
		}

		return getMyAnswers();
	}

	public Collection<ChallengeAnswer> deleteAnswer(Long answerId) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
			em.getTransaction().begin();
			ChallengeAnswer answer = em.find(ChallengeAnswer.class, answerId);
			em.remove(answer);
			em.getTransaction().commit();
		}

		return getMyAnswers();
	}
	
	public VoteCount vote(Long answerId, VoteRequest voteData) {
		if (hasAccess(Role.PROJECT_MEMBER)) {
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
		}
		return project;
	}

	/** editor actions **/

	public Project updateActivity(ChallengeActivityRequest activityData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			activityData.update(activity);
			em.getTransaction().commit();
		}

		return project;
	}

	public Project createField(ChallengeFieldRequest fieldData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			em.getTransaction().begin();
			ChallengeField field = new ChallengeField();
			fieldData.update(field);
			activity.getFields().add(field);
			em.getTransaction().commit();
		}
		return project;
	}

	public Project updateField(Long fieldId, ChallengeFieldRequest fieldData) {
		if (hasAccess(Role.PROJECT_EDITOR)) {
			ChallengeField field = em.find(ChallengeField.class, fieldId);

			em.getTransaction().begin();
			fieldData.update(field);
			em.getTransaction().commit();
		}
		return project;
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

}
