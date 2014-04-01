package org.greengin.senseitweb.logic.data;

import org.greengin.senseitweb.entities.data.AbstractDataProjectItem;
import org.greengin.senseitweb.entities.data.DataCollectionActivity;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.project.AbstractActivityActions;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteManager;
import org.greengin.senseitweb.logic.voting.VoteRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public abstract class DataActions<E extends AbstractDataProjectItem, F extends AbstractDataProjectItem, T extends DataCollectionActivity<E, F>>
        extends AbstractActivityActions<T> {

    private static final String ITEMS_QUERY = "SELECT d FROM %s d WHERE d.dataStore = :dataStore";

    Class<E> dataType;
    Class<F> analysisType;


    public DataActions(Long projectId, Class<T> type, Class<E> dataType, Class<F> analysisType, SubscriptionManager subscriptionManager, FileManager fileManager, UserProfile user, boolean tokenOk, EntityManager em) {
        super(projectId, type, subscriptionManager, fileManager, user, tokenOk, em);
        setTypes(dataType, analysisType);
    }

    public DataActions(Long projectId, Class<T> type, Class<E> dataType, Class<F> analysisType, SubscriptionManager subscriptionManager, FileManager fileManager, UsersManager usersManager, EntityManager em, HttpServletRequest request) {
        super(projectId, type, subscriptionManager, fileManager, usersManager, em, request);
        setTypes(dataType, analysisType);
    }

    private void setTypes(Class<E> dataType, Class<F> analysisType) {
        this.dataType = dataType;
        this.analysisType = analysisType;
    }

    /**
     * member actions *
     */

    private <K extends AbstractDataProjectItem> Collection<K> getItems(Class<K> type) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            String queryStr = String.format(ITEMS_QUERY, type.getName());
            TypedQuery<K> query = em.createQuery(queryStr, type);
            query.setParameter("dataStore", activity);
            Collection<K> list = query.getResultList();
            for (VotableEntity item : list) {
                item.setSelectedVoteAuthor(user);
            }
            return list;
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> NewDataItemResponse<K> createItem(Class<K> type,
                                                                                  DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            try {

                K item = type.newInstance();
                item.setDataStore(activity);
                item.setAuthor(user);
                if (manipulator.onCreate(project, activity, item)) {
                    em.getTransaction().begin();
                    em.persist(item);
                    em.getTransaction().commit();
                }

                NewDataItemResponse<K> response = new NewDataItemResponse<K>();
                response.setNewItemId(item.getId());
                response.setItems(getItems(type));
                return response;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> K updateItem(Class<K> type, Long itemId,
                                                             DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            K item = em.find(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId() == user.getId()) {
                em.getTransaction().begin();
                try {
                    manipulator.onUpdate(project, activity, item);
                    em.getTransaction().commit();
                    return item;
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> Long deleteItem(Class<K> type, Long itemId,
                                                                DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            K item = em.find(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId() == user.getId()) {
                try {
                    em.getTransaction().begin();
                    manipulator.onDelete(project, activity, item);
                    em.remove(item);
                    em.getTransaction().commit();
                    return itemId;
                } catch (Exception e) {
                    em.getTransaction().rollback();
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public Collection<E> getData() {
        return getItems(dataType);
    }

    public NewDataItemResponse<E> createData(DataItemManipulator<T, E> manipulator) {
        return createItem(dataType, manipulator);
    }

    public Long deleteData(Long itemId, DataItemManipulator<T, E> manipulator) {
        return deleteItem(dataType, itemId, manipulator);
    }


    public Collection<F> getAnalysis() {
        return getItems(analysisType);
    }

    public NewDataItemResponse<F> createAnalysis(DataItemManipulator<T, F> manipulator) {
        return createItem(analysisType, manipulator);
    }

    public F updateAnalysis(Long itemId, DataItemManipulator<T, F> manipulator) {
        return updateItem(analysisType, itemId, manipulator);
    }

    public VoteCount voteItem(Long itemId, VoteRequest voteData) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            AbstractDataProjectItem item = em.find(AbstractDataProjectItem.class, itemId);
            if (item != null && item.getDataStore() == activity) {
                VoteManager voter = new VoteManager(em, user, item);
                return voter.vote(voteData);
            }
        }

        return null;
    }


}