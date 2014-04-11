package org.greengin.senseitweb.controllers.projects;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.JacksonObjectMapper;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.users.AccessLevel;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;
import org.greengin.senseitweb.logic.rating.CommentRequest;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/project/{projectId}")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,    // 1 MB
        maxFileSize = 1024 * 1024,          // 1 MB
        maxRequestSize = 1024 * 1024 * 10) // 10MB
public class ProjectController {

    @Autowired
    ContextBean context;

    @Autowired
    JacksonObjectMapper objectMapper;

    private ProjectActions createProjectManager(Long projectId, HttpServletRequest request) {
        return new ProjectActions(context, projectId, request);
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ProjectResponse get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        Project project = createProjectManager(projectId, request).get();
        AccessLevel access = context.getSubscriptionManager().getAccessLevel(project);

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

        EntityManager em = context.createEntityManager();
        return em.find(Project.class, projectId);
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


    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.User.class)
    public List<Comment> comments(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).getComments();
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.User.class)
    public List<Comment> commentsPost(@PathVariable("projectId") Long projectId, @RequestBody CommentRequest data, HttpServletRequest request) {
        return createProjectManager(projectId, request).comment(data);
    }

    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseView(value = Views.User.class)
    public List<Comment> commentsDelete(@PathVariable("projectId") Long projectId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        return createProjectManager(projectId, request).deleteComment(commentId);
    }

}
