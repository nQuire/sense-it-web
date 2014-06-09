package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.FileMapUpload;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UserProfileActions extends AbstractContentManager {

    static final String UPDATE_USER_CONNECTIONS = "UPDATE UserConnection SET userId = ? WHERE userId = ?";
    static final String DELETE_USER_CONNECTION = "DELETE FROM UserConnection WHERE userId = ? AND providerId = ?";


    public UserProfileActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public UserProfileActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }


    public boolean updateProfileImage(StatusResponse currentStatus, FileMapUpload files) {
        if (user != null && loggedWithToken && currentStatus.getProfile() != null &&
                user.getId().equals(currentStatus.getProfile().getId()) && files.getData().containsKey("image")) {

            FileMapUpload.FileData file = files.getData().get("image");
            String fileContext = currentStatus.getProfile().getId().toString();
            String filename = null;
            if (file != null) {
                try {
                    filename = context.getFileManager().uploadFile(fileContext, file.filename, file.data);
                } catch (IOException ignored) {
                }
            }

            EntityManager em = context.createEntityManager();
            em.getTransaction().begin();
            currentStatus.getProfile().setImage(filename);
            em.getTransaction().commit();
            return true;
        }
        return false;
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

    public boolean deleteConnection(StatusResponse currentStatus, String providerId) {
        if (user != null && loggedWithToken && currentStatus.getProfile() != null) {

            if ((currentStatus.getProfile().getPassword() != null && currentStatus.getProfile().getPassword().length() > 0)
                    || currentStatus.getConnections().size() > 1) {
                EntityManager em = context.createEntityManager();

                em.getTransaction().begin();

                Query query = em.createNativeQuery(DELETE_USER_CONNECTION);
                query.setParameter(1, currentStatus.getProfile().getUsername());
                query.setParameter(2, providerId);
                query.executeUpdate();

                em.getTransaction().commit();

                currentStatus.getConnections().remove(providerId);
            }

            return true;
        }
        return false;
    }


    public boolean setPassword(StatusResponse currentStatus, PasswordRequest data) {
        if (user != null && loggedWithToken && currentStatus.getProfile() != null &&
                data.getNewPassword() != null) {

            if ((user.isPasswordSet() && context.getUsersManager().matchPassword(user, data.getOldPassword())) ||
                    (!user.isPasswordSet() && "".equals(data.getOldPassword()))) {

                if (data.getNewPassword().length() < 6) {
                    currentStatus.getResponses().put("newpassword", "too_short");
                } else if (data.getNewPassword().equals(user.getUsername())) {
                    currentStatus.getResponses().put("newpassword", "same_as_username");
                } else {
                    EntityManager em = context.createEntityManager();

                    em.getTransaction().begin();
                    context.getUsersManager().setPassword(user, data.getNewPassword());
                    em.getTransaction().commit();
                }
            } else {
                currentStatus.getResponses().put("oldpassword", "bad_password");
            }

            return true;
        }
        return false;
    }

}
