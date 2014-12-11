package org.greengin.nquireit.dao;


import org.greengin.nquireit.entities.activities.base.AbstractActivity;
import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.data.DataItemManipulator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;

@Component
public class DataActivityDao {

    private static final String ITEMS_QUERY = "SELECT d FROM %s d WHERE d.dataStore = :dataStore";
    private static final String ITEM_COUNT_QUERY = "SELECT COUNT(d) FROM %s d WHERE d.dataStore = :dataStore";


    @PersistenceContext
    EntityManager em;


    public <K extends AbstractDataProjectItem, T extends AbstractActivity> Collection<K> itemList(Class<K> type, T activity) {
        String queryStr = String.format(ITEMS_QUERY, type.getName());
        TypedQuery<K> query = em.createQuery(queryStr, type);
        query.setParameter("dataStore", activity);
        return query.getResultList();
    }

    public <K extends AbstractDataProjectItem, T extends AbstractActivity> Long itemCount(Class<K> type, T activity) {
        String queryStr = String.format(ITEM_COUNT_QUERY, type.getName());

        TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
        query.setParameter("dataStore", activity);
        return query.getSingleResult();
    }


    public <K extends AbstractDataProjectItem> K getItem(Class<K> type, Long itemId) {
        return em.find(type, itemId);
    }


    @Transactional
    public <K extends AbstractDataProjectItem> void updateItem(K item, DataItemManipulator<?, K> manipulator) {
        em.persist(item);
        manipulator.onUpdate(item);
    }

    @Transactional
    public <K extends AbstractDataProjectItem> void removeItem(K item) {
        em.persist(item);
        em.remove(item);
    }

    @Transactional
    public <K extends AbstractDataProjectItem> K createItem(UserProfile author, Class<K> type, DataItemManipulator<?, K> manipulator) throws Exception {
        K item = type.newInstance();
        item.setDataStore(manipulator.getActivity());
        item.setAuthor(author);
        item.setDate(new Date());

        if (manipulator.onCreate(item)) {
            em.persist(item);
            return item;
        } else {
            return null;
        }
    }
}