package org.greengin.senseitweb.tests;

import junit.framework.Assert;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.rating.CommentThreadEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.voting.VoteManagerBean;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class CommentTests extends TestsBase {

    CommentThreadEntity thread;
    UserProfile user1;
    UserProfile user2;



    @Before
    public void before() {
        super.before();
        UserProfile author = helper.createUser("author");
        thread = helper.createProject(author, "project", ProjectType.CHALLENGE);
        user1 = helper.createUser("user1");
        user2 = helper.createUser("user2");

    }

    @Test
    public void testNoComments() {
        assertNotNull(thread.getComments());
        assertEquals(0, thread.getComments().size());
    }


}
