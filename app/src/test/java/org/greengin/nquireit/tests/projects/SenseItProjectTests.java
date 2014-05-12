package org.greengin.nquireit.tests.projects;


import org.greengin.nquireit.entities.activities.senseit.SenseItActivity;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.project.senseit.SenseItActivityActions;
import org.greengin.nquireit.logic.project.senseit.SenseItProfileRequest;
import org.greengin.nquireit.tests.TestsBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
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
        projectId = helper.createProject(author, ProjectType.SENSEIT).getId();
        member = helper.createUser("member");
        member2 = helper.createUser("member2");
        nonMember = helper.createUser("other");

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();
        projectActions(projectId, member2).join();
    }


    private SenseItActivityActions senseItActions(UserProfile user) {
        return new SenseItActivityActions(context, projectId, user, true);
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

    @Test
    public void testFields() {

    }

}
