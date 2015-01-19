package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.logs.Log;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Component
public class LogsDao {
    static final String RECENT_USERS_QUERY = "SELECT DISTINCT l.actor FROM Log l WHERE l.timestamp > :time ORDER BY l.timestamp DESC";
    static final String CHECK_RECENT_USER_QUERY = "SELECT l.type FROM Log l WHERE l.timestamp > :time AND l.actor = :user ORDER BY l.timestamp DESC";

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Boolean log(UserProfile user, String type, String path, AbstractEntity object, UserProfile owner, String value) {
        Log log = new Log();
        log.setTimestamp(new Date());
        log.setActor(user);
        log.setType(type);
        log.setPath(path);
        log.setObject(object);
        log.setOwner(owner);
        log.setValue(value);
        em.persist(log);

        return true;
    }


    public List<UserProfile> getRecentUsers(long sessionTimeout) {
        TypedQuery<UserProfile> query = em.createQuery(RECENT_USERS_QUERY, UserProfile.class);
        Date time = new Date(new Date().getTime() - sessionTimeout);
        query.setParameter("time", time);

        Vector<UserProfile> users = new Vector<UserProfile>();

        for (UserProfile user : query.getResultList()) {
            if (userRecentAction(user, sessionTimeout)) {
                users.add(user);
            }
        }
        return users;
    }

    public boolean userRecentAction(UserProfile user, long sessionTimeout) {
        TypedQuery<String> query = em.createQuery(CHECK_RECENT_USER_QUERY, String.class);
        Date time = new Date(new Date().getTime() - sessionTimeout);
        query.setParameter("time", time);
        query.setParameter("user", user);
        query.setMaxResults(1);
        List<String> results = query.getResultList();
        return results.size() == 1 && !"logged-out".equals(results.get(0));
    }
}
