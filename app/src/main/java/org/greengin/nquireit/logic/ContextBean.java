package org.greengin.nquireit.logic;

import lombok.Getter;
import lombok.Setter;
import org.greengin.nquireit.dao.*;
import org.greengin.nquireit.logic.data.FileManagerBean;
import org.greengin.nquireit.logic.users.SubscriptionManagerBean;
import org.greengin.nquireit.dao.CommentsDao;
import org.greengin.nquireit.dao.VoteDao;
import org.greengin.nquireit.logic.users.UserServiceBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by evilfer on 4/2/14.
 */
public class ContextBean implements InitializingBean {

    @Getter
    static ContextBean context;


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
    UserProfileDao userProfileDao;

    @Autowired
    @Getter
    VoteDao voteDao;

    @Autowired
    @Getter
    CommentsDao commentsDao;

    @Autowired
    @Getter
    ForumDao forumDao;

    @Autowired
    @Getter
    ProjectDao projectDao;

    @Autowired
    @Getter
    DataActivityDao dataActivityDao;

    @Autowired
    @Getter
    SenseItDao senseItDao;

    @Autowired
    @Getter
    ChallengeDao challengeDao;

    @Autowired
    @Getter
    SpotItDao spotItDao;

    @Autowired
    @Getter
    TextDao textDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (httpProxyHost.length() > 0 && httpProxyPort.length() > 0) {
            System.getProperties().put("http.proxyHost", httpProxyHost);
            System.getProperties().put("http.proxyPort", httpProxyPort);
        }

        context = this;
    }
}
