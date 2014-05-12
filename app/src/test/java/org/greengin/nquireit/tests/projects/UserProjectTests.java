package org.greengin.nquireit.tests.projects;


import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.users.AccessLevel;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.tests.TestsBase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class UserProjectTests extends TestsBase {


    @Test
    public void testUserExists() {
        helper.createUser("u");
        List<UserProfile> users = helper.users();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testUserCreateProject() {
        UserProfile user = helper.createUser("author");
        Project project = helper.createProject(user);
        Assert.assertNotNull(project);

        AccessLevel access = context.getSubscriptionManager().getAccessLevel(project, user);

        Assert.assertTrue(access.isAdmin());
        Assert.assertFalse(access.isMember());
    }

    @Test
    public void testOpenProject() {
        UserProfile user = helper.createUser("author");
        Project project = helper.createProject(user);
        Long projectId = project.getId();

        Assert.assertFalse(project.getOpen());

        projectActions(projectId, user).setOpen(true);
        Assert.assertTrue(helper.getProject(projectId).getOpen());

        projectActions(projectId, user).setOpen(false);
        Assert.assertFalse(helper.getProject(projectId).getOpen());
    }

    @Test
    public void testDeleteProject() {
        UserProfile user = helper.createUser("author");
        Project project = helper.createProject(user);
        Long projectId = project.getId();

        Assert.assertNotNull(helper.getProject(projectId));

        projectActions(projectId, user).deleteProject();
        Assert.assertNull(helper.getProject(projectId));
    }


    @Test
    public void testJoinProject() {
        UserProfile author = helper.createUser("author");
        UserProfile member = helper.createUser("member");

        Long projectId = helper.createProject(author).getId();

        AccessLevel before = accessLevel(projectId, member);
        Assert.assertFalse(before.isAdmin());
        Assert.assertFalse(before.isMember());

        projectActions(projectId, member).join();

        AccessLevel afterJoin1 = accessLevel(projectId, member);
        Assert.assertFalse(afterJoin1.isAdmin());
        Assert.assertFalse(afterJoin1.isMember());

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();

        AccessLevel afterJoin2 = accessLevel(projectId, member);
        Assert.assertFalse(afterJoin2.isAdmin());
        Assert.assertTrue(afterJoin2.isMember());

        projectActions(projectId, member).leave();

        AccessLevel afterLeave = accessLevel(projectId, member);
        Assert.assertFalse(afterLeave.isAdmin());
        Assert.assertFalse(afterLeave.isMember());
    }

    @Test
    public void testAuthorModifiesProject() {
        UserProfile user = helper.createUser("author");
        Long projectId = helper.createProject(user).getId();

        ProjectRequest request = new ProjectRequest();
        request.setTitle("project2");
        projectActions(projectId, user).updateMetadata(request, null);

        Assert.assertEquals("project2", helper.getProject(projectId).getTitle());
    }

    @Test
    public void testAuthorModifiesAfterOpenProject() {
        UserProfile user = helper.createUser("author");
        Long projectId = helper.createProject(user).getId();

        projectActions(projectId, user).setOpen(true);

        ProjectRequest request = new ProjectRequest();
        request.setTitle("project2");
        projectActions(projectId, user).updateMetadata(request, null);

        Assert.assertEquals("project1", helper.getProject(projectId).getTitle());
    }

    @Test
    public void testNotAccessProjectModification() {
        UserProfile author = helper.createUser("author");
        Long projectId = helper.createProject(author).getId();
        UserProfile other = helper.createUser("other");

        ProjectRequest request = new ProjectRequest();
        request.setTitle("!!!");

        projectActions(projectId, other).updateMetadata(request, null);
        Assert.assertFalse("!!!".equals(helper.getProject(projectId).getTitle()));

        projectActions(projectId, other).setOpen(true);
        Assert.assertFalse(helper.getProject(projectId).getOpen());

        projectActions(projectId, other).deleteProject();
        Assert.assertNotNull(helper.getProject(projectId));
    }

}
