package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.AbstractContentManager;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

public class UserProfileActions extends AbstractContentManager {


    public UserProfileActions(ContextBean context, UserProfile user, boolean tokenOk) {
        super(context, user, tokenOk);
    }

    public UserProfileActions(ContextBean context, HttpServletRequest request) {
        super(context, request);
    }


    public boolean updateProfileImage(StatusResponse currentStatus, FileMapUpload files) {
        return user != null && loggedWithToken && currentStatus.getProfile() != null && user.getId().equals(currentStatus.getProfile().getId()) && context.getUsersManager().updateProfileImage(currentStatus, files);
    }

    public boolean updateProfile(StatusResponse currentStatus, ProfileRequest data) {
        return user != null && loggedWithToken && currentStatus.getProfile() != null && user.getId().equals(currentStatus.getProfile().getId()) && context.getUsersManager().updateProfile(currentStatus, data);
    }

    public boolean deleteConnection(StatusResponse currentStatus, String providerId) {
        return user != null && loggedWithToken && currentStatus.getProfile() != null && context.getUsersManager().deleteConnection(currentStatus, providerId);
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
                    context.getUsersManager().setPassword(user, data.getNewPassword());
                }
            } else {
                currentStatus.getResponses().put("oldpassword", "bad_password");
            }

            return true;
        }
        return false;
    }

}
