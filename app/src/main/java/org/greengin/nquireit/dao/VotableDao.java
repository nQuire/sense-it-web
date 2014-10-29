package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.rating.*;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;
import org.greengin.nquireit.logic.log.LogManagerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


@Component
public class VotableDao {
    private static final String REPORTED_CONTENT_QUERY = "SELECT t, COUNT(v.id) as N FROM VotableEntity t INNER JOIN t.votes v WHERE v.value = -2 GROUP BY t.id";
    private static final String REPORTING_VOTE_QUERY = "SELECT v FROM Vote v WHERE v.target = :target AND v.value = -2";

    @Autowired
    ForumDao forumDao;

    @Autowired
    CommentsDao commentsDao;

    @Autowired
    LogManagerBean logManager;

    @PersistenceContext
    EntityManager em;

    public HashMap<String, List<ReportedContent>> getReportedContent(ContextBean context) {
        HashMap<String, List<ReportedContent>> categories = new HashMap<String, List<ReportedContent>>();

        TypedQuery<Object[]> query = em.createQuery(REPORTED_CONTENT_QUERY, Object[].class);
        for (Object[] result : query.getResultList()) {
            VotableEntity entity = (VotableEntity) result[0];
            long count = (Long) result[1];

            String type = entity.getReportedType(context);
            if (type != null) {
                if (!categories.containsKey(type)) {
                    categories.put(type, new Vector<ReportedContent>());
                }
                List<ReportedContent> list = categories.get(type);

                ReportedContent info = new ReportedContent();
                info.setId(entity.getId());
                info.setCount(count);
                entity.createReportedInfo(info, context);
                list.add(info);
            }
        }

        return categories;
    }

    public boolean deleteReportedEntity(UserProfile admin, Long id) {
        VotableEntity entity = em.find(VotableEntity.class, id);

        if (entity != null) {
            if (entity instanceof Comment) {
                Comment comment = (Comment) entity;
                logManager.reportedContentRemoved(admin, comment);
                commentsDao.deleteComment(comment);
                return true;
            } else if (entity instanceof ForumThread) {
                logManager.reportedContentRemoved(admin, entity);
                forumDao.deleteForumThread((ForumThread) entity);
                return true;
            }
        }

        return false;
    }

    @Transactional
    public boolean approveReportedEntity(UserProfile admin, Long id) {
        VotableEntity entity = em.find(VotableEntity.class, id);
        if (entity != null) {
            TypedQuery<Vote> query = em.createQuery(REPORTING_VOTE_QUERY, Vote.class);
            query.setParameter("target", entity);
            boolean wasReported = false;
            for (Vote v : query.getResultList()) {
                v.setValue(0l);
                wasReported = true;
            }

            if (wasReported) {
                logManager.reportedContentApproved(admin, entity);
            }

            return true;
        } else {
            return false;
        }
    }
}
