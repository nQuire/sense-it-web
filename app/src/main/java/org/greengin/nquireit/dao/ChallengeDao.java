package org.greengin.nquireit.dao;

import lombok.Getter;
import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.activities.challenge.*;
import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.activities.senseit.SenseItProfile;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.projects.ProjectMetadataBlock;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.project.challenge.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;


@Component
public class ChallengeDao extends UtilsDao {

    private static final String FIELD_ACTIVITY_QUERY = "SELECT f FROM ChallengeActivity ac INNER JOIN ac.fields f WHERE ac = :activity AND f.id = :id";

    private static final String ANSWER_ACTIVITY_USER_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author AND an.id = :id";

    private static final String ANSWER_ACTIVITY_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.id = :id";

    private static final String MY_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";

    private static final String ALL_ANSWERS_QUERY = "SELECT an FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND (an.author = :author OR an.published = TRUE)";

    private static final String ANSWER_COUNT_QUERY = "SELECT COUNT(an) AS N FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE ac = :activity AND an.author = :author";

    private static final String FIND_ACTIVITY_QUERY = "SELECT ac FROM ChallengeActivity ac INNER JOIN ac.answers an WHERE an = :answer";


    @PersistenceContext
    @Getter
    EntityManager em;


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

    public ChallengeActivity findActivity(ChallengeAnswer answer) {
        TypedQuery<ChallengeActivity> query = em.createQuery(FIND_ACTIVITY_QUERY, ChallengeActivity.class);
        query.setParameter("answer", answer);
        return query.getSingleResult();
    }

    @Transactional
    public List<ChallengeAnswer> getAnswers(boolean onlyMine, AbstractActivity activity, UserProfile user) {
        TypedQuery<ChallengeAnswer> query = em.createQuery(onlyMine ? MY_ANSWERS_QUERY : ALL_ANSWERS_QUERY,
                ChallengeAnswer.class);
        query.setParameter("activity", activity);
        query.setParameter("author", user);

        return query.getResultList();
    }

    @Transactional
    public Long createAnswer(ChallengeActivity activity, UserProfile user, ChallengeAnswerRequest answerData) {
        ChallengeAnswer answer = new ChallengeAnswer();
        answer.setAuthor(user);
        answer.setDate(new Date());
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
        em.persist(activity);
        activity.setStage(stage);
    }

    @Transactional
    public void setProposalIdeaVisibility(ChallengeActivity activity, boolean visible) {
        em.persist(activity);
        activity.setAnswersAlwaysVisible(visible);
    }

    @Transactional
    public void updateActivity(ChallengeActivity activity, ChallengeActivityRequest activityData) {
        em.persist(activity);
        activityData.update(activity);
    }

    @Transactional
    public void createActivityField(ChallengeActivity activity, ChallengeFieldRequest fieldData) {
        em.persist(activity);

        ChallengeField field = new ChallengeField();
        fieldData.update(field);
        field.setWeight(activity.getFields().size());
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
        em.persist(activity);
        activity.getFields().remove(getField(activity, fieldId));
    }

    @Transactional
    public void updateActivityOutcome(ChallengeActivity activity, ChallengeOutcomeRequest outcomeData) {
        em.persist(activity.getOutcome());
        outcomeData.update(em, activity.getOutcome());
    }

    public void initProject(Project project) {

        ProjectMetadataBlock link = new ProjectMetadataBlock();
        link.setTitle("Mission goal");
        link.setContent("This mission challenges users to propose answers to a specific question.");
        project.getMetadata().getBlocks().add(link);

        ProjectMetadataBlock process = new ProjectMetadataBlock();
        process.setTitle("Mission process");
        process.setContent("<ul>" +
                "<li>The <b>proposal stage</b> will be open until <code>[edit]</code>. Participants can submit their ideas until that date.</li>" +
                "<li>Then, participants can vote their favourite submissions.</li>" +
                "<li>Finally, the winner will be announced on <code>[edit]</code>.</li>");
        project.getMetadata().getBlocks().add(process);

        ChallengeActivity activity = (ChallengeActivity) project.getActivity();
        ChallengeField field = new ChallengeField();
        field.setLabel("Title");
        field.setType(ChallengeFieldType.TEXTFIELD);
        em.persist(field);
        activity.getFields().add(field);
    }

    @Transactional
    public void moveActivityField(ChallengeActivity activity, Long fieldId, ChallengeFieldMoveRequest fieldData) {
        em.persist(activity);
        this.move(activity.getFields(), fieldId, fieldData.getUp());
    }

    @Transactional
    public void submitAnswer(ChallengeActivity activity, UserProfile user, Long answerId, boolean published) {
        ChallengeAnswer answer = getAnswer(activity, user, answerId);
        if (answer != null) {
            answer.setPublished(published);
        }
    }

}
