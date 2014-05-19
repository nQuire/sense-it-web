package org.greengin.nquireit.logic.data;

import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.activity.AbstractActivityActions;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

public abstract class DataActions<E extends AbstractDataProjectItem, F extends AbstractDataProjectItem, T extends DataCollectionActivity<E, F>>
        extends AbstractActivityActions<T> {

    private static final String ITEMS_QUERY = "SELECT d FROM %s d WHERE d.dataStore = :dataStore";
    private static final String ITEM_COUNT_QUERY = "SELECT COUNT(d) FROM %s d WHERE d.dataStore = :dataStore";

    Class<E> dataType;
    Class<F> analysisType;


    public DataActions(ContextBean context, Long projectId, Class<T> type, Class<E> dataType, Class<F> analysisType, UserProfile user, boolean tokenOk) {
        super(context, projectId, type, user, tokenOk);
        setTypes(dataType, analysisType);
    }

    public DataActions(ContextBean context, Long projectId, Class<T> type, Class<E> dataType, Class<F> analysisType, HttpServletRequest request) {
        super(context, projectId, type, request);
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
            EntityManager em = context.createEntityManager();

            String queryStr = String.format(ITEMS_QUERY, type.getName()); TypedQuery<K> query = em.createQuery(queryStr, type); query.setParameter("dataStore", activity); Collection<K> list = query.getResultList(); for (VotableEntity item : list)
                item.setSelectedVoteAuthor(user);
            return list;
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> Long getItemCount(Class<K> type) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            EntityManager em = context.createEntityManager();

            String queryStr = String.format(ITEM_COUNT_QUERY, type.getName());

            TypedQuery<Long> query = em.createQuery(queryStr, Long.class);
            query.setParameter("dataStore", activity);
            return query.getSingleResult();
        }

        return 0l;
    }


    private <K extends AbstractDataProjectItem> NewDataItemResponse<K> createItem(Class<K> type, DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            try {
                K item = type.newInstance();
                item.setDataStore(activity);
                item.setAuthor(user);
                item.setDate(new Date());

                if (manipulator.onCreate(project, activity, item)) {
                    EntityManager em = context.createEntityManager();
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
            EntityManager em = context.createEntityManager();
            K item = em.find(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId().equals(user.getId())) {
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
            EntityManager em = context.createEntityManager();
            K item = em.find(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId().equals(user.getId())) {
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

    public Long getDataCount() {
        return getItemCount(dataType);
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
            EntityManager em = context.createEntityManager();
            AbstractDataProjectItem item = em.find(AbstractDataProjectItem.class, itemId);
            if (item != null && item.getDataStore() == activity) {
                return context.getVoteManager().vote(user, item, voteData);
            }
        }

        return null;
    }


}
