package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.RoleType;
import org.greengin.nquireit.entities.users.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class SubscriptionManagerBean {

    @Autowired
    RoleManagerBean roleManager;

    @Autowired
    UserServiceBean usersManager;


    public long projectUserCount(Project project, RoleType type) {
        return roleManager.contextUserCount(project, type);
    }

    public List<UserProfile> projectUsers(Project project, RoleType type) {
        return roleManager.contextUsers(project, type);
    }

    public List<UserProfile> projectAdmins(Project project) {
        return projectUsers(project, RoleType.ADMIN);
    }

    public List<UserProfile> projectMembers(Project project) {
        return projectUsers(project, RoleType.MEMBER);
    }


    public void projectCreatedInTransaction(EntityManager em, Project project, UserProfile user) {
        roleManager.addRoleInTransaction(em, project, user, RoleType.ADMIN);
    }


    public AccessLevel getAccessLevel(Project project, UserProfile user) {
        AccessLevel level = new AccessLevel();


        if (project != null && user != null) {
            if (user.isAdmin()) {
                level.setAdmin(true);
            }

            for (RoleType role : roleManager.userRoles(user, project)) {
                switch (role) {
                    case ADMIN:
                        level.setAdmin(true);
                        break;
                    case MEMBER:
                        level.setMember(true);
                        break;
                }
            }
        }

        return level;
    }

    public boolean is(RoleType type, Project project, UserProfile user) {
        if (project != null && user != null) {
            return roleManager.is(type, project, user);
        }

        return false;
    }


    public void subscribe(UserProfile user, Project project, RoleType type) {
        roleManager.addRole(user, project, type);
    }

    public void unsubscribe(UserProfile user, Project project, RoleType type) {
        roleManager.removeRole(user, project, type);
    }
}
