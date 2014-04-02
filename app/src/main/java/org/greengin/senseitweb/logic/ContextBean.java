package org.greengin.senseitweb.logic;

import lombok.Getter;
import org.greengin.senseitweb.logic.data.FileManagerBean;
import org.greengin.senseitweb.logic.permissions.SubscriptionManagerBean;
import org.greengin.senseitweb.logic.permissions.UsersManagerBean;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.greengin.senseitweb.logic.rating.CommentManagerBean;
import org.greengin.senseitweb.logic.rating.VoteManagerBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * Created by evilfer on 4/2/14.
 */
public class ContextBean {
    @Autowired
    @Getter
    UsersManagerBean usersManager;


    @Autowired
    @Getter
    SubscriptionManagerBean subscriptionManager;

    @Autowired
    @Getter
    FileManagerBean fileManager;

    @Autowired
    @Getter
    VoteManagerBean voteManager;

    @Autowired
    @Getter
    CommentManagerBean commentManager;

    @Autowired
    CustomEntityManagerFactoryBean entityManagerFactory;

    public EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
