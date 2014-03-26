package org.greengin.senseitweb.controllers.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.logic.project.ProjectListActions;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/projects")
public class ProjectListController {

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    UsersManager usersManager;

    @Autowired
    CustomEntityManagerFactory entityManagerFactory;


    private ProjectListActions createProjectManager(HttpServletRequest request) {
        return new ProjectListActions(subscriptionManager, usersManager, entityManagerFactory.createEntityManager(), request);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<Project> projectList(HttpServletRequest request) {
		return createProjectManager(request).getProjects();
	}
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Long create(@RequestBody ProjectCreationRequest projectData, HttpServletRequest request) {
        return createProjectManager(request).createProject(projectData);
	}

    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public Map<Long, AccessLevel> getAccess(@RequestBody List<Long> projectIds, HttpServletRequest request) {
		HashMap<Long, AccessLevel> levels = new HashMap<Long, AccessLevel>();
		
		UserProfile user = usersManager.currentUser(request);
		EntityManager em = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		
		for (Long id : projectIds) {
			levels.put(id, subscriptionManager.getAccessLevel(em, user, id));
		}
		
		return levels;
    }

    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    @ResponseBody
    public Map<Long, ProjectResponse> reload(@RequestBody List<Long> projectIds, HttpServletRequest request) {
		HashMap<Long, ProjectResponse> projects = new HashMap<Long, ProjectResponse>();
		
		UserProfile user = usersManager.currentUser(request);
		EntityManager em = entityManagerFactory.createEntityManager();
		
		for (Long id : projectIds) {
			ProjectResponse pr = new ProjectResponse();
			pr.setProject(em.find(Project.class, id));
			pr.setAccess(subscriptionManager.getAccessLevel(em, user, id));
			projects.put(id, pr);
		}
		
		return projects;
    }
	
}
