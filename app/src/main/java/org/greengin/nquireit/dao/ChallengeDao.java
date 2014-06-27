package org.greengin.nquireit.dao;

import lombok.Getter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivity;
import org.greengin.nquireit.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.nquireit.entities.activities.challenge.ChallengeAnswer;
import org.greengin.nquireit.entities.activities.challenge.ChallengeField;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.nquireit.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.nquireit.logic.project.challenge.ChallengeFieldRequest;
import org.greengin.nquireit.logic.project.challenge.ChallengeOutcomeRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Component
public class ChallengeDao {

    private static final String FIELD_ACTIVITY_QUERY = "SELECT f FROM ChallengeActivity ac INNER JOIN ac.fields f WHERE ac = :activity AND f.id = :id";

    private static final String ANSWER_ACTIVITY_USER_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author AND an.id = :id";

    private static final String ANSWER_ACTIVITY_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.id = :id";

    private static final String MY_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";

    private static final String ALL_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity";

    private static final String ANSWER_COUNT_QUERY = "SELECT COUNT(an) AS N FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";


    @PersistenceContext
    @Getter
    EntityManager em;

    public SenseItSeries getSeries(Long dataId) {
        return em.find(SenseItSeries.class, dataId);
    }

    private SenseItProfile profile(SenseItActivity activity) {
        if (activity.getProfile() == null) {
            activity.setProfile(new SenseItProfile());
        }

        return activity.getProfile();
    }

    public ChallengeField getField(AbstractActivity activity, Long id) {
        TypedQuery<ChallengeField> query = em.createQuery(FIELD_ACTIVITY_QUERY, ChallengeField.class);
        query.setParameter("activity", activity);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public ChallengeAnswer getAnswer(AbstractActivity activity, Long id) {
        TypedQuery<ChallengeAnswer> query = em.createQuery(ANSWER_ACTIVITY_QUERY, ChallengeAnswer.class);
        query.setParameter("activity", activity);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private ChallengeAnswer getAnswer(AbstractActivity activity, UserProfile author, Long id) {
        TypedQuery<ChallengeAnswer> query = em.createQuery(ANSWER_ACTIVITY_USER_QUERY, ChallengeAnswer.class);
        query.setParameter("activity", activity);
        query.setParameter("author", author);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long answerCount(AbstractActivity activity, UserProfile user) {
        TypedQuery<Long> query = em.createQuery(ANSWER_COUNT_QUERY, Long.class);
        query.setParameter("activity", activity);
        query.setParameter("author", user);
        return query.getSingleResult();
    }

    @Transactional
    public List<ChallengeAnswer> getAnswers(boolean onlyMine, AbstractActivity activity, UserProfile user) {
        TypedQuery<ChallengeAnswer> query = em.createQuery(onlyMine ? MY_ANSWERS_QUERY : ALL_ANSWERS_QUERY,
                ChallengeAnswer.class);
        query.setParameter("activity", activity);
        if (onlyMine) {
            query.setParameter("author", user);
        }
        return query.getResultList();
    }

    @Transactional
    public Long createAnswer(ChallengeActivity activity, UserProfile user, ChallengeAnswerRequest answerData) {
        ChallengeAnswer answer = new ChallengeAnswer();
        answer.setAuthor(user);
        answerData.update(answer);
        activity.getAnswers().add(answer);
        em.persist(answer);
        return answer.getId();
    }

    @Transactional
    public void updateAnswer(ChallengeActivity activity, UserProfile user, Long answerId, ChallengeAnswerRequest answerData) {
        ChallengeAnswer answer = getAnswer(activity, user, answerId);
        if (answer != null) {
            answerData.update(answer);
        }
    }

    @Transactional
    public void deleteAnswer(ChallengeActivity activity, UserProfile user, Long answerId) {
        ChallengeAnswer answer = getAnswer(activity, user, answerId);
        if (answer != null) {
            if (activity.getOutcome().getSelectedAnswer() == answer) {
                activity.getOutcome().setSelectedAnswer(null);
            }
            activity.getAnswers().remove(answer);
            em.remove(answer);
        }
    }


    @Transactional
    public void setActivityStage(ChallengeActivity activity, ChallengeActivityStage stage) {
        activity.setStage(stage);
    }

    @Transactional
    public void updateActivity(ChallengeActivity activity, ChallengeActivityRequest activityData) {
        activityData.update(activity);
    }

    @Transactional
    public void createActivityField(ChallengeActivity activity, ChallengeFieldRequest fieldData) {
        ChallengeField field = new ChallengeField();
        fieldData.update(field);
        activity.getFields().add(field);
    }

    @Transactional
    public void updateActivityField(ChallengeActivity activity, Long fieldId, ChallengeFieldRequest fieldData) {
        ChallengeField field = getField(activity, fieldId);
        if (field != null) {
            fieldData.update(field);
        }
    }

    @Transactional
    public void deleteActivityField(ChallengeActivity activity, Long fieldId) {
        activity.getFields().remove(getField(activity, fieldId));
    }

    @Transactional
    public void updateActivityOutcome(ChallengeActivity activity, ChallengeOutcomeRequest outcomeData) {
        outcomeData.update(em, activity.getOutcome());
    }
}
