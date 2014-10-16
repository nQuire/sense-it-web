package org.greengin.nquireit.logic.data;

import org.greengin.nquireit.entities.data.AbstractDataProjectItem;
import org.greengin.nquireit.entities.data.DataCollectionActivity;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.users.PermissionType;
import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.activity.AbstractActivityActions;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

public abstract class DataActions<E extends AbstractDataProjectItem, F extends AbstractDataProjectItem, T extends DataCollectionActivity<E, F>>
        extends AbstractActivityActions<T> {

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
        if (hasAccess(PermissionType.PROJECT_BROWSE)) {
            return context.getDataActivityDao().itemList(type, activity);
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> Long getItemCount(Class<K> type) {
        if (hasAccess(PermissionType.PROJECT_BROWSE)) {
            return context.getDataActivityDao().itemCount(type, activity);
        }

        return 0l;
    }


    private <K extends AbstractDataProjectItem> NewDataItemResponse<K> createItem(Class<K> type, DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            try {
                manipulator.init(project, activity);

                K newItem = context.getDataActivityDao().createItem(user, type, manipulator);
                if (newItem != null) {
                    NewDataItemResponse<K> response = new NewDataItemResponse<K>();
                    response.setNewItemId(newItem.getId());
                    response.setItems(getItems(type));
                    return response;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> K updateItem(Class<K> type, Long itemId,
                                                             DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {

            K item = context.getDataActivityDao().getItem(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId().equals(user.getId())) {
                manipulator.init(project, activity);
                context.getDataActivityDao().updateItem(item, manipulator);
                return item;
            }
        }

        return null;
    }

    private <K extends AbstractDataProjectItem> Long deleteItem(Class<K> type, Long itemId,
                                                                DataItemManipulator<T, K> manipulator) {
        if (hasAccess(PermissionType.PROJECT_MEMBER_ACTION)) {
            K item = context.getDataActivityDao().getItem(type, itemId);
            if (item != null && item.getDataStore() == activity && item.getAuthor().getId().equals(user.getId())) {
                manipulator.init(project, activity);
                context.getDataActivityDao().removeItem(item, manipulator);
                return itemId;
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
        NewDataItemResponse<E> response = createItem(dataType, manipulator);
        if (response != null) {
            context.getLogManager().data(user, project, response.getNewItemId(), true);
        }
        return response;
    }

    public Long deleteData(Long itemId, DataItemManipulator<T, E> manipulator) {
        Long id = deleteItem(dataType, itemId, manipulator);
        if (id != null) {
            context.getLogManager().data(user, project, id, false);
        }
        return id;
    }

    public E updateData(Long itemId, DataItemManipulator<T, E> manipulator) {
        return updateItem(dataType, itemId, manipulator);
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
            AbstractDataProjectItem item = context.getDataActivityDao().getItem(AbstractDataProjectItem.class, itemId);
            if (item != null && item.getDataStore() == activity) {
                return context.getVoteDao().vote(user, item, voteData);
            }
        }

        return null;
    }


    public List<Comment> getDataComments(Long itemId) {
        if (hasAccess(PermissionType.PROJECT_BROWSE)) {
            E item = context.getDataActivityDao().getItem(dataType, itemId);
            return item.getComments();
        }
        return null;
    }

    public List<Comment> commentData(Long itemId, CommentRequest data) {

        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            E item = context.getDataActivityDao().getItem(dataType, itemId);
            if (item != null) {
                context.getCommentsDao().comment(user, item, data);
                return item.getComments();
            }
        }

        return null;
    }

    public List<Comment> deleteDataComment(Long itemId, Long commentId) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            E item = context.getDataActivityDao().getItem(dataType, itemId);
            if (item != null && context.getCommentsDao().deleteComment(user, item, commentId)) {
                return item.getComments();
            }
        }

        return null;
    }

    public VoteCount voteDataComment(Long itemId, Long commentId, VoteRequest voteData) {
        if (hasAccess(PermissionType.PROJECT_COMMENT)) {
            E item = context.getDataActivityDao().getItem(dataType, itemId);
            if (item != null) {
                Comment comment = context.getCommentsDao().getComment(item, commentId);
                if (comment != null) {
                    return context.getVoteDao().vote(user, comment, voteData);
                }
            }
        }

        return null;
    }
}
