package org.greengin.senseitweb.controllers.projects;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.persistence.EMF;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RequestMapping(value = "/project/{projectId}")
public class ProjectController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ProjectResponse get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        EntityManager em = EMF.get().createEntityManager();
        Project project = em.find(Project.class, projectId);
        AccessLevel access = SubscriptionManager.get().getAccessLevel(project, request);

        ProjectResponse response = new ProjectResponse();
        response.setProject(project);
        response.setAccess(access);
        return response;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Boolean delete(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.deleteProject();
    }

    @RequestMapping(value = "/metadata", method = RequestMethod.PUT)
    @ResponseBody
    public Project update(@PathVariable("projectId") Long projectId, ProjectRequest projectData, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.updateMetadata(projectData);
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    public AccessLevel join(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.join();
    }

    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    @ResponseBody
    public AccessLevel leave(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.leave();
    }

    @RequestMapping(value = "/admin/open", method = RequestMethod.PUT)
    @ResponseBody
    public Project open(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.setOpen(true);
    }

    @RequestMapping(value = "/admin/close", method = RequestMethod.PUT)
    @ResponseBody
    public Project close(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.setOpen(false);
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(Views.User.class)
    public Collection<UserProfile> users(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        ProjectActions manager = new ProjectActions(projectId, request);
        return manager.getUsers();
    }

}
