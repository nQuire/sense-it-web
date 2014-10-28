package org.greengin.nquireit.dao;

import org.greengin.nquireit.entities.AbstractEntity;
import org.greengin.nquireit.entities.logs.Log;
import org.greengin.nquireit.entities.users.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Component
public class LogsDao {

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


}
