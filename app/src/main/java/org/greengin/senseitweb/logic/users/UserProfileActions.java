package org.greengin.senseitweb.logic.users;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.users.PermissionType;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.AbstractContentManager;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;
import org.greengin.senseitweb.logic.rating.CommentRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class UserProfileActions extends AbstractContentManager {

    static final String UPDATE_USER_CONNECTIONS = "UPDATE UserConnection SET userId = ? WHERE userId = ?";



    public UserProfileActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public UserProfileActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }


    public boolean updateProfile(StatusResponse currentStatus, ProfileRequest data) {
        if (user != null && loggedWithToken && currentStatus.getProfile() != null && data.getUsername() != null) {

            if (!data.getUsername().equals(currentStatus.getProfile().getUsername())) {

                if (data.getUsername().length() == 0) {
                    currentStatus.getResponses().put("username", "username_empty");
                } else if (!context.getUsersManager().usernameIsAvailable(data.getUsername())) {
                    currentStatus.getResponses().put("username", "username_not_available");
                } else {
                    EntityManager em = context.createEntityManager();

                    em.getTransaction().begin();

                    Query query = em.createNativeQuery(UPDATE_USER_CONNECTIONS);
                    query.setParameter(1, data.getUsername());
                    query.setParameter(2, currentStatus.getProfile().getUsername());
                    query.executeUpdate();

                    currentStatus.getProfile().setUsername(data.getUsername());

                    em.getTransaction().commit();
                }
            }

            return true;
        } else {
            return false;
        }
    }

}
