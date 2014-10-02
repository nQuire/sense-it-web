package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.ReportedContent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


@Component
public class VotableDao {
    private static final String REPORTED_CONTENT_QUERY = "SELECT t, COUNT(v.id) as N FROM VotableEntity t INNER JOIN t.votes v WHERE v.value = -2 GROUP BY t.id";

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
}
