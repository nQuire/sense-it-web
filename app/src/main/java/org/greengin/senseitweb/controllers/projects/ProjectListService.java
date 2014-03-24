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
import org.greengin.senseitweb.logic.project.ProjectCreationRequest;
import org.greengin.senseitweb.logic.project.ProjectListActions;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.persistence.EMF;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/projects")
public class ProjectListService {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<Project> projectList(HttpServletRequest request) {
		ProjectListActions manager = new ProjectListActions(request);
		return manager.getProjects();
	}
	
	@RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Long create(ProjectCreationRequest projectData, HttpServletRequest request) {
		ProjectListActions manager = new ProjectListActions(request);
		return manager.createProject(projectData);
	}

    @RequestMapping(value = "/access", method = RequestMethod.POST)
    @ResponseBody
    public Map<Long, AccessLevel> getAccess(List<Long> projectIds, HttpServletRequest request) {
		HashMap<Long, AccessLevel> levels = new HashMap<Long, AccessLevel>();
		
		UserProfile user = UsersManager.get().currentUser(request);
		EntityManager em = EMF.get().createEntityManager();
		
		for (Long id : projectIds) {
			levels.put(id, SubscriptionManager.get().getAccessLevel(em, user, id));
		}
		
		return levels;
    }

    @RequestMapping(value = "/reload", method = RequestMethod.POST)
    @ResponseBody
    public Map<Long, ProjectResponse> reload(List<Long> projectIds, HttpServletRequest request) {
		HashMap<Long, ProjectResponse> projects = new HashMap<Long, ProjectResponse>();
		
		UserProfile user = UsersManager.get().currentUser(request);
		EntityManager em = EMF.get().createEntityManager();
		
		for (Long id : projectIds) {
			ProjectResponse pr = new ProjectResponse();
			pr.setProject(em.find(Project.class, id));
			pr.setAccess(SubscriptionManager.get().getAccessLevel(em, user, id));
			projects.put(id, pr);
		}
		
		return projects;
    }
	
}
