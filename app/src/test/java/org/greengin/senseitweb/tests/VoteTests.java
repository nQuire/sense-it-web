package org.greengin.senseitweb.tests;

import junit.framework.Assert;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.rating.VoteCount;
import org.greengin.senseitweb.logic.rating.VoteRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class VoteTests extends TestsBase {

    VotableEntity votable;
    UserProfile user1;
    UserProfile user2;

    protected VoteCount count(UserProfile user) {
        votable.setSelectedVoteAuthor(user);
        return votable.getVoteCount();
    }

    protected void vote(UserProfile user, Long value) {
        VoteRequest request = new VoteRequest();
        request.setValue(value);
        context.getVoteManager().vote(user, votable, request);
    }

    protected void test(UserProfile user, int positive, int negative) {
        test(user, positive, negative, false, 0l);
    }

    protected void test(UserProfile user, int positive, int negative, Long myValue) {
        test(user, positive, negative, true, myValue);
    }


    protected void test(UserProfile user, int positive, int negative, boolean myVote, Long myValue) {
        VoteCount count = count(user);
        Assert.assertEquals(positive + negative, count.getTotal());
        Assert.assertEquals(positive, count.getPositive());
        Assert.assertEquals(negative, count.getNegative());

        if (myVote) {
            Assert.assertNotNull(count.getMyVote());
            Assert.assertEquals(count.getMyVote().getValue(), myValue);
        } else {
            Assert.assertNull(count.getMyVote());
        }
    }

    @Before
    public void before() {
        super.before();
        UserProfile author = helper.createUser("author");
        votable = helper.createProject(author, ProjectType.CHALLENGE);
        user1 = helper.createUser("user1");
        user2 = helper.createUser("user2");

    }

    @Test
    public void testNoVotes() {
        test(user1, 0, 0);
        test(user2, 0, 0);
    }

    @Test
    public void testOneVote() {
        vote(user1, 1l);
        test(user1, 1, 0, 1l);
        test(user2, 1, 0);
    }

    @Test
    public void testVoteChange() {
        vote(user1, 1l);

        vote(user1, -1l);
        test(user1, 0, 1, -1l);
        test(user2, 0, 1);

        vote(user1, 0l);
        test(user1, 0, 0, 0l);
        test(user2, 0, 0);
    }

    @Test
    public void testTwoVotes() {
        vote(user1, 1l);
        vote(user2, -1l);

        test(user1, 1, 1, 1l);
        test(user2, 1, 1, -1l);
    }



}
