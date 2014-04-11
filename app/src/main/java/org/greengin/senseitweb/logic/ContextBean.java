package org.greengin.senseitweb.logic;

import lombok.Getter;
import lombok.Setter;
import org.greengin.senseitweb.logic.data.FileManagerBean;
import org.greengin.senseitweb.logic.users.SubscriptionManagerBean;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.greengin.senseitweb.logic.rating.CommentManagerBean;
import org.greengin.senseitweb.logic.rating.VoteManagerBean;
import org.greengin.senseitweb.logic.users.UserServiceBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

/**
 * Created by evilfer on 4/2/14.
 */
public class ContextBean implements InitializingBean {

    @Setter
    private String httpProxyHost;

    @Setter
    private String httpProxyPort;

    @Autowired
    @Getter
    UserServiceBean usersManager;


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

    @Override
    public void afterPropertiesSet() throws Exception {
        if (httpProxyHost.length() > 0 && httpProxyPort.length() > 0) {
            System.getProperties().put("http.proxyHost", httpProxyHost);
            System.getProperties().put("http.proxyPort", httpProxyPort);
        }
    }
}
