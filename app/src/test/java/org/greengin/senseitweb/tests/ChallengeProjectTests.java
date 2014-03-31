package org.greengin.senseitweb.tests;


import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class ChallengeProjectTests extends TestsBase {

    UserProfile author;
    UserProfile member;
    UserProfile member2;
    UserProfile nonMember;
    Long projectId;

    @Before
    public void before() {
        super.before();
        author = helper.createUser("author");
        projectId = helper.createProject(subscriptionManager, author, "challenge", ProjectType.CHALLENGE).getId();
        member = helper.createUser("member");
        member2 = helper.createUser("member2");
        nonMember = helper.createUser("other");

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();
        projectActions(projectId, member2).join();
    }


    private ChallengeActivityActions challengeActions(UserProfile user) {
        return new ChallengeActivityActions(projectId, subscriptionManager, fileManager, user, true, entityManagerFactory.createEntityManager());
    }

    private ChallengeActivity activity() {
        return (ChallengeActivity) helper.getProject(projectId).getActivity();
    }

    private void setMaxQuestions(int max) {
        ChallengeActivityRequest request = new ChallengeActivityRequest();
        request.setMaxAnswers(max);
        challengeActions(author).updateActivity(request);
    }


    @Test
    public void testSetUp() {
        AccessLevel authorAccess = accessLevel(projectId, author);
        AccessLevel memberAccess = accessLevel(projectId, member);
        AccessLevel member2Access = accessLevel(projectId, member2);
        AccessLevel nonMemberAccess = accessLevel(projectId, nonMember);

        Assert.assertTrue(authorAccess.isAuthor());
        Assert.assertTrue(authorAccess.isAdmin());
        Assert.assertFalse(authorAccess.isMember());

        Assert.assertFalse(memberAccess.isAuthor());
        Assert.assertFalse(memberAccess.isAdmin());
        Assert.assertTrue(memberAccess.isMember());

        Assert.assertFalse(member2Access.isAuthor());
        Assert.assertFalse(member2Access.isAdmin());
        Assert.assertTrue(member2Access.isMember());

        Assert.assertFalse(nonMemberAccess.isAuthor());
        Assert.assertFalse(nonMemberAccess.isAdmin());
        Assert.assertFalse(nonMemberAccess.isMember());
    }

    @Test
    public void testDefaultMaxQuestions() {
        Assert.assertEquals(1, ChallengeActivity.DEFAULT_MAX);
    }

    @Test
    public void testMaxQuestions() {
        Assert.assertEquals(ChallengeActivity.DEFAULT_MAX, activity().getMaxAnswers().longValue());

        setMaxQuestions(2);
        Assert.assertEquals(ChallengeActivity.DEFAULT_MAX, activity().getMaxAnswers().longValue());

        challengeActions(author).setOpen(false);
        setMaxQuestions(2);
        Assert.assertEquals(2, activity().getMaxAnswers().longValue());
    }

    @Test
    public void testDuringProposal() {
        Assert.assertEquals(activity().getStage(), ChallengeActivityStage.PROPOSAL);

        ChallengeAnswerRequest request = new ChallengeAnswerRequest();

        Assert.assertEquals(0, challengeActions(member).getAnswersForParticipant().size());
        challengeActions(member).createAnswer(request);
        Assert.assertEquals(1, challengeActions(member).getAnswersForParticipant().size());

        Assert.assertEquals(0, challengeActions(member2).getAnswersForParticipant().size());

        Assert.assertNull(challengeActions(nonMember).getAnswersForParticipant());
    }

    @Test
    public void testDuringVoting() {
        Assert.assertEquals(activity().getStage(), ChallengeActivityStage.PROPOSAL);
        ChallengeAnswerRequest request = new ChallengeAnswerRequest();
        challengeActions(member).createAnswer(request);
        challengeActions(author).setStage(ChallengeActivityStage.VOTING);

        Assert.assertEquals(1, challengeActions(member).getAnswersForParticipant().size());
        Assert.assertEquals(1, challengeActions(member2).getAnswersForParticipant().size());

        challengeActions(member).createAnswer(request);
        Assert.assertEquals(1, challengeActions(member).getAnswersForParticipant().size());
        Assert.assertEquals(1, challengeActions(member2).getAnswersForParticipant().size());

        Assert.assertNull(challengeActions(nonMember).getAnswersForParticipant());
    }


}
