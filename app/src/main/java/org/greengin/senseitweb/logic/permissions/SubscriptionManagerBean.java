package org.greengin.senseitweb.logic.permissions;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.RoleType;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class SubscriptionManagerBean {

    @Autowired
    RoleManagerBean roleManager;

    @Autowired
    UsersManagerBean usersManager;


    @Autowired
    CustomEntityManagerFactoryBean entityManagerFactory;

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
        roleManager.addRoleInTransaction(em, project, user, RoleType.AUTHOR);
        roleManager.addRoleInTransaction(em, project, user, RoleType.ADMIN);
    }

    public AccessLevel getAccessLevel(Project project, HttpServletRequest request) {
        UserProfile user = usersManager.currentUser(request);
        return getAccessLevel(project, user);
    }

    public AccessLevel getAccessLevel(Long projectId, EntityManager em, UserProfile user) {
        return getAccessLevel(em.find(Project.class, projectId), user);
    }

    public AccessLevel getAccessLevel(Project project, UserProfile user) {
        AccessLevel level = new AccessLevel();

        if (project != null && user != null) {
            for (RoleType role : roleManager.userRoles(user, project)) {
                switch (role) {
                    case AUTHOR:
                        level.setAuthor(true);
                        break;
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


    public void subscribe(EntityManager em, UserProfile user, Project project, RoleType type) {
        roleManager.addRole(em, user, project, RoleType.MEMBER);
    }

    public void unsubscribe(EntityManager em, UserProfile user, Project project, RoleType type) {
        roleManager.removeRole(em, user, project, type);
    }
}
