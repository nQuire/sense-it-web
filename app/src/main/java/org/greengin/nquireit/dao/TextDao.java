package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.base.TextItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evilfer on 8/11/14.
 */
public class TextDao {
    static final String TEXT_KEY_QUERY = "SELECT t FROM TextItem t WHERE t.textId=:textId";
    static final String TEXT_QUERY = "SELECT t FROM TextItem t";

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Boolean setText(String textId, String content) {
        TypedQuery<TextItem> query = em.createQuery(TEXT_KEY_QUERY, TextItem.class);
        query.setParameter("textId", textId);
        List<TextItem> items = query.getResultList();
        if (items.size() == 0) {
            TextItem item = new TextItem();
            item.setTextId(textId);
            item.setContent(content);
            em.persist(item);
        } else {
            TextItem item = items.get(0);
            item.setContent(content);

            for (int i = 1; i < items.size(); i++) {
                em.remove(items.get(i));
            }
        }

        return true;
    }

    public Map<String, String> getTexts() {
        Map<String, String> texts = new HashMap<String, String>();
        TypedQuery<TextItem> query = em.createQuery(TEXT_QUERY, TextItem.class);
        for (TextItem item : query.getResultList()) {
            texts.put(item.getTextId(), item.getContent());
        }
        return texts;
    }
}
