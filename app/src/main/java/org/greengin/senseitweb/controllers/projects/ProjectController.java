package org.greengin.senseitweb.controllers.projects;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.JacksonObjectMapper;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.FileManager;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactory;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.persistence.EntityManager;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/project/{projectId}")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,    // 1 MB
        maxFileSize = 1024 * 1024,          // 1 MB
        maxRequestSize = 1024 * 1024 * 10) // 10MB
public class ProjectController {

    @Autowired
    SubscriptionManager subscriptionManager;

    @Autowired
    CustomEntityManagerFactory entityManagerFactory;

    @Autowired
    FileManager fileManager;

    @Autowired
    UsersManager usersManager;

    @Autowired
    JacksonObjectMapper objectMapper;

    private ProjectActions createProjectManager(Long projectId, HttpServletRequest request) {
        return new ProjectActions(projectId, subscriptionManager, fileManager, usersManager, entityManagerFactory.createEntityManager(), request);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ProjectResponse get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        EntityManager em = entityManagerFactory.createEntityManager();
        Project project = em.find(Project.class, projectId);
        AccessLevel access = subscriptionManager.getAccessLevel(project, request);

        ProjectResponse response = new ProjectResponse();
        response.setProject(project);
        response.setAccess(access);
        return response;
    }


    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Boolean delete(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).deleteProject();
    }

    @RequestMapping(value = "/metadata", method = RequestMethod.POST)
    @ResponseBody
    public Project update(@PathVariable("projectId") Long projectId, @RequestBody ProjectRequest projectData, HttpServletRequest request) {
        return createProjectManager(projectId, request).updateMetadata(projectData, null);
    }

    @RequestMapping(value = "/metadata/files", method = RequestMethod.POST)
    @ResponseBody
    public Project updateFiles(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        try {
            DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
            ProjectRequest projectData = objectMapper.readValue(multiPartRequest.getParameter("body"), ProjectRequest.class);
            FileMapUpload files = new FileMapUpload();

            for (String param : multiPartRequest.getParameterMap().keySet()) {
                if (!"body".equals(param)) {
                    files.getData().put(param, null);
                }
            }

            for (Map.Entry<String, MultipartFile> entry : multiPartRequest.getFileMap().entrySet()) {
                files.add(entry.getKey(), entry.getValue().getOriginalFilename(), entry.getValue().getInputStream());
            }


            return createProjectManager(projectId, request).updateMetadata(projectData, files);

        } catch (IOException e) {
            e.printStackTrace();
        }

        EntityManager em = entityManagerFactory.createEntityManager();
        Project project = em.find(Project.class, projectId);
        return project;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    public AccessLevel join(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).join();
    }

    @RequestMapping(value = "/leave", method = RequestMethod.POST)
    @ResponseBody
    public AccessLevel leave(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).leave();
    }

    @RequestMapping(value = "/admin/open", method = RequestMethod.PUT)
    @ResponseBody
    public Project open(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).setOpen(true);
    }

    @RequestMapping(value = "/admin/close", method = RequestMethod.PUT)
    @ResponseBody
    public Project close(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).setOpen(false);
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.User.class)
    public Collection<UserProfile> users(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).getUsers();
    }

}