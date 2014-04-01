package org.greengin.senseitweb.tests;


import org.greengin.senseitweb.controllers.users.ProfileRequest;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivityStage;
import org.greengin.senseitweb.entities.activities.senseit.SenseItActivity;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItProfileRequest;
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
public class SenseItProjectTests extends TestsBase {

    UserProfile author;
    UserProfile member;
    UserProfile member2;
    UserProfile nonMember;
    Long projectId;

    @Before
    public void before() {
        super.before();
        author = helper.createUser("author");
        projectId = helper.createProject(subscriptionManager, author, "senseit", ProjectType.SENSEIT).getId();
        member = helper.createUser("member");
        member2 = helper.createUser("member2");
        nonMember = helper.createUser("other");

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();
        projectActions(projectId, member2).join();
    }


    private SenseItActivityActions senseItActions(UserProfile user) {
        return new SenseItActivityActions(projectId, subscriptionManager, fileManager, user, true, entityManagerFactory.createEntityManager());
    }

    private SenseItActivity activity() {
        return (SenseItActivity) helper.getProject(projectId).getActivity();
    }


    @Test
    public void testGeolocation() {
        Assert.assertFalse(activity().getProfile().getGeolocated());

        SenseItProfileRequest request = new SenseItProfileRequest();
        request.setGeolocated(true);

        senseItActions(author).updateProfile(request);
        Assert.assertFalse(activity().getProfile().getGeolocated());

        senseItActions(author).setOpen(false);
        senseItActions(author).updateProfile(request);
        Assert.assertTrue(activity().getProfile().getGeolocated());

        request.setGeolocated(false);
        senseItActions(member).updateProfile(request);
        Assert.assertTrue(activity().getProfile().getGeolocated());

        senseItActions(nonMember).updateProfile(request);
        Assert.assertTrue(activity().getProfile().getGeolocated());
    }

}
