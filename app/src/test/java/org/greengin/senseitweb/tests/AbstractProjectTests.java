package org.greengin.senseitweb.tests;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by evilfer on 4/1/14.
 */
public abstract class AbstractProjectTests extends TestsBase {

    String projectName;
    ProjectType type;

    UserProfile author;
    UserProfile member;
    UserProfile member2;
    UserProfile nonMember;
    Long projectId;


    protected Project project() {
        return helper.getProject(projectId);
    }

    protected ProjectActions projectActions(UserProfile user) {
        return projectActions(projectId, user);
    }

    public AbstractProjectTests(String projectName, ProjectType type) {
        this.projectName = projectName;
        this.type = type;
    }

    @Before
    public void before() {
        super.before();
        author = helper.createUser("author");
        projectId = helper.createProject(subscriptionManager, author, projectName, type).getId();
        member = helper.createUser("member");
        member2 = helper.createUser("member2");
        nonMember = helper.createUser("other");

        projectActions(projectId, author).setOpen(true);
        projectActions(projectId, member).join();
        projectActions(projectId, member2).join();
        projectActions(projectId, author).setOpen(false);
    }

    @Test
    public void projectSetUp() {
        Assert.assertFalse(project().getOpen());
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

    protected void testProjectEdition(ProjectManipulator manipulator) {
        manipulator.testSame();

        projectActions(author).setOpen(true);

        manipulator.modify(author);
        manipulator.testSame();

        manipulator.modify(member);
        manipulator.testSame();

        manipulator.modify(nonMember);
        manipulator.testSame();

        projectActions(author).setOpen(false);

        manipulator.modify(author);
        manipulator.testModified();

        manipulator.reset(author);
        manipulator.testSame();

        manipulator.modify(member);
        manipulator.testSame();

        manipulator.modify(nonMember);
        manipulator.testSame();
    }

    protected void testPrivateMemberAction(ProjectManipulator manipulator) {
        manipulator.testSame();

        projectActions(author).setOpen(false);

        manipulator.modify(author);
        manipulator.testSame();

        manipulator.modify(member);
        manipulator.testSame();

        manipulator.modify(nonMember);
        manipulator.testSame();

        projectActions(author).setOpen(true);

        manipulator.modify(author);
        manipulator.testSame();

        manipulator.modify(nonMember);
        manipulator.testSame();

        manipulator.modify(member);
        manipulator.testModified();

        manipulator.reset(member2);
        manipulator.testModified();

        manipulator.reset(member);
        manipulator.testSame();
    }


    public interface ProjectManipulator {
        void modify(UserProfile user);
        void reset(UserProfile user);
        void testModified();
        void testSame();
    }


}
