package org.greengin.senseitweb.tests;


import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.tests.helpers.DbHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
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
        Project project = helper.createProject(subscriptionManager, user);
        Assert.assertNotNull(project);

        AccessLevel access = subscriptionManager.getAccessLevel(project, user);

        Assert.assertTrue(access.isAuthor());
        Assert.assertTrue(access.isAdmin());
        Assert.assertFalse(access.isMember());
    }

    @Test
    public void testOpenProject() {
        UserProfile user = helper.createUser("author");
        Project project = helper.createProject(subscriptionManager, user);
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
        Project project = helper.createProject(subscriptionManager, user);
        Long projectId = project.getId();

        Assert.assertNotNull(helper.getProject(projectId));

        projectActions(projectId, user).deleteProject();
        Assert.assertNull(helper.getProject(projectId));
    }


    @Test
    public void testJoinProject() {
        UserProfile author = helper.createUser("author");
        UserProfile member = helper.createUser("member");

        Long projectId = helper.createProject(subscriptionManager, author).getId();

        AccessLevel before = accessLevel(projectId, member);
        Assert.assertFalse(before.isAuthor());
        Assert.assertFalse(before.isAdmin());
        Assert.assertFalse(before.isMember());

        projectActions(projectId, member).join();

        AccessLevel afterJoin1 = accessLevel(projectId, member);
        Assert.assertFalse(afterJoin1.isAuthor());
        Assert.assertFalse(afterJoin1.isAdmin());
        Assert.assertFalse(afterJoin1.isMember());

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();

        AccessLevel afterJoin2 = accessLevel(projectId, member);
        Assert.assertFalse(afterJoin2.isAuthor());
        Assert.assertFalse(afterJoin2.isAdmin());
        Assert.assertTrue(afterJoin2.isMember());

        projectActions(projectId, member).leave();

        AccessLevel afterLeave = accessLevel(projectId, member);
        Assert.assertFalse(afterLeave.isAuthor());
        Assert.assertFalse(afterLeave.isAdmin());
        Assert.assertFalse(afterLeave.isMember());
    }

    @Test
    public void testAuthorModifiesProject() {
        UserProfile user = helper.createUser("author");
        Long projectId = helper.createProject(subscriptionManager, user).getId();

        ProjectRequest request = new ProjectRequest();
        request.setTitle("project2");
        projectActions(projectId, user).updateMetadata(request, null);

        Assert.assertEquals("project2", helper.getProject(projectId).getTitle());
    }

    @Test
    public void testAuthorModifiesAfterOpenProject() {
        UserProfile user = helper.createUser("author");
        Long projectId = helper.createProject(subscriptionManager, user).getId();

        projectActions(projectId, user).setOpen(true);

        ProjectRequest request = new ProjectRequest();
        request.setTitle("project2");
        projectActions(projectId, user).updateMetadata(request, null);

        Assert.assertEquals("project1", helper.getProject(projectId).getTitle());
    }

    @Test
    public void testNotAccessProjectModification() {
        UserProfile author = helper.createUser("author");
        Long projectId = helper.createProject(subscriptionManager, author).getId();
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
