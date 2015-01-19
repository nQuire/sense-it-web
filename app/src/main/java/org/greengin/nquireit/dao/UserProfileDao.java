package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.greengin.nquireit.logic.files.FileMapUpload;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class UserProfileDao {

    static final String ALL_USERS_QUERY = "SELECT u FROM UserProfile u";
    static final String AUTHORITY_QUERY = "SELECT userId FROM UserConnection WHERE providerId = ? AND providerUserId = ?";
    static final String USER_QUERY = "SELECT u from UserProfile u WHERE LOWER(u.username)=LOWER(:username)";
    static final String USER_EMAIL_QUERY = "SELECT u from UserProfile u WHERE LOWER(u.email)=LOWER(:email)";
    static final String UPDATE_USER_CONNECTIONS = "UPDATE UserConnection SET userId = ? WHERE userId = ?";
    static final String DELETE_USER_CONNECTION = "DELETE FROM UserConnection WHERE userId = ? AND providerId = ?";

    @PersistenceContext
    EntityManager em;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileManagerBean fileManager;


    public UserProfile loadUserById(Long id) {
        return em.find(UserProfile.class, id);
    }

    public UserProfile loadUserByUsernameOrEmail(String username, String email) throws UsernameNotFoundException {
        try {
            return loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return loadUserByEmail(email);
        }
    }

    public UserProfile loadUserByUsername(String s) throws UsernameNotFoundException {
        TypedQuery<UserProfile> query = em.createQuery(USER_QUERY, UserProfile.class);

        query.setParameter("username", s);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(s);
        }
    }

    public UserProfile loadUserByEmail(String email) throws UsernameNotFoundException {
        TypedQuery<UserProfile> query = em.createQuery(USER_EMAIL_QUERY, UserProfile.class);

        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(email);
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
    public UserProfile createUser(String username, String password, String email, boolean emailConfirmed) {
        UserProfile user = new UserProfile();
        user.setUsername(username);
        user.setEmail(email);
        user.setEmailConfirmed(emailConfirmed);
        user.setDate(new Date());
        user.setPassword(password != null ? passwordEncoder.encode(password) : null);
        em.persist(user);

        return user;
    }

    public UserProfile user(UserProfile user) {
        return em.contains(user) ? user : em.find(UserProfile.class, user.getId());
    }

    @Transactional
    public void setPassword(UserProfile user, String password) {
        em.persist(user);
        user.setPassword(passwordEncoder.encode(password));
    }


    public boolean matchPassword(UserProfile user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public boolean updateProfileImage(UserProfile user, FileMapUpload files) {
        if (files.getData().containsKey("image")) {
            em.persist(user);

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
    public void updateUserInformation(UserProfile user, HashMap<String, String> metadata, HashMap<String, Boolean> visibility) {
        em.persist(user);

        if (metadata != null) {
            HashMap<String, String> current = user.getMetadata();
            if (current == null) {
                current = new HashMap<String, String>();
            }
            current.putAll(metadata);
            user.setMetadata(current);
        }

        if (visibility != null) {
            user.setVisibility(visibility);
        }
    }

    public List<UserProfile> listUsers() {
        return em.createQuery(ALL_USERS_QUERY, UserProfile.class).getResultList();
    }

    @Transactional
    public void setAdmin(Long userId, boolean isAdmin) {
        UserProfile user = em.find(UserProfile.class, userId);
        if (user != null) {
            user.setAdmin(isAdmin);
        }
    }

    @Transactional
    public void deleteUser(UserProfile user) {
        for (String providerId : new String[]{"google", "twitter", "facebook"}) {
            deleteConnection(user, providerId);
        }
        em.persist(user);
        em.remove(user);
    }
}
