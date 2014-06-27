package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.users.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.HashMap;

@Component
public class UserProfileDao {

    static final String AUTHORITY_QUERY = "SELECT userId FROM UserConnection WHERE providerId = ? AND providerUserId = ?";
    static final String USER_QUERY = "SELECT u from UserProfile u WHERE LOWER(u.username)=LOWER(:username)";
    static final String UPDATE_USER_CONNECTIONS = "UPDATE UserConnection SET userId = ? WHERE userId = ?";
    static final String DELETE_USER_CONNECTION = "DELETE FROM UserConnection WHERE userId = ? AND providerId = ?";

    @PersistenceContext
    EntityManager em;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileManagerBean fileManager;


    public UserProfile loadUserByUsername(String s) throws UsernameNotFoundException {
        TypedQuery<UserProfile> query = em.createQuery(USER_QUERY, UserProfile.class);

        query.setParameter("username", s);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(s);
        }
    }

    public UserProfile loadUserByProviderUserId(String providerId, String id) {

        Query query = em.createNativeQuery(AUTHORITY_QUERY);

        try {
            query.setParameter(1, providerId);
            query.setParameter(2, id);
            Object obj = query.getSingleResult();
            return loadUserByUsername((String) obj);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public UserProfile createUser(String username, String password) {
        UserProfile user = new UserProfile();
        user.setUsername(username);
        user.setPassword(password != null ? passwordEncoder.encode(password) : null);
        em.persist(user);

        return user;
    }

    public UserProfile user(Long userId) {
        return em.find(UserProfile.class, userId);
    }

    @Transactional
    public void setPassword(Long userId, String password) {
        user(userId).setPassword(passwordEncoder.encode(password));
    }


    public boolean matchPassword(UserProfile user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public boolean updateProfileImage(UserProfile user, FileMapUpload files) {
        if (files.getData().containsKey("image")) {

            FileMapUpload.FileData file = files.getData().get("image");
            String fileContext = user.getId().toString();
            String filename = null;
            if (file != null) {
                try {
                    filename = fileManager.uploadFile(fileContext, file.filename, file.data);
                } catch (IOException ignored) {
                }
            }

            user.setImage(filename);
            return true;
        }
        return false;
    }


    @Transactional
    public boolean deleteConnection(UserProfile user, String providerId) {
        em.persist(user);
        Query query = em.createNativeQuery(DELETE_USER_CONNECTION);
        query.setParameter(1, user.getUsername());
        query.setParameter(2, providerId);
        query.executeUpdate();

        em.getTransaction().commit();
        return true;
    }

    @Transactional
    public void updateUsername(UserProfile user, String username) {
        em.persist(user);

        Query query = em.createNativeQuery(UPDATE_USER_CONNECTIONS);
        query.setParameter(1, username);
        query.setParameter(2, user.getUsername());
        query.executeUpdate();

        user.setUsername(username);
    }

    @Transactional
    public void updateUserMetadata(UserProfile user, HashMap<String, String> metadata) {
        em.persist(user);

        HashMap<String, String> current = user.getMetadata();
        if (current == null) {
            current = new HashMap<String, String>();
        }

        current.putAll(metadata);

        user.setMetadata(metadata);
    }
}
