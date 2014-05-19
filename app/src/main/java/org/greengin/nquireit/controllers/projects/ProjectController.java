package org.greengin.nquireit.controllers.projects;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.JacksonObjectMapper;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.files.RequestsUtils;
import org.greengin.nquireit.logic.users.AccessLevel;
import org.greengin.nquireit.logic.project.ProjectActions;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
    @ResponseView(value = Views.UserName.class)
    public ProjectResponse get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).get();
    }


    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public Boolean delete(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).deleteProject();
    }

    @RequestMapping(value = "/metadata", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public ProjectResponse update(@PathVariable("projectId") Long projectId, @RequestBody ProjectRequest projectData, HttpServletRequest request) {
        return createProjectManager(projectId, request).updateMetadata(projectData, null);
    }

    @RequestMapping(value = "/metadata/files", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public ProjectResponse updateFiles(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        try {
            ProjectRequest projectData = RequestsUtils.readParam(request, objectMapper, "body", ProjectRequest.class);
            FileMapUpload files = RequestsUtils.getFiles(request);
            return createProjectManager(projectId, request).updateMetadata(projectData, files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
    @ResponseView(value = Views.UserName.class)
    public ProjectResponse open(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).setOpen(true);
    }

    @RequestMapping(value = "/admin/close", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public ProjectResponse close(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).setOpen(false);
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public Collection<UserProfile> users(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).getUsers();
    }


    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public List<Comment> comments(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createProjectManager(projectId, request).getComments();
    }

    @RequestMapping(value = "/comments", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public List<Comment> commentsPost(@PathVariable("projectId") Long projectId, @RequestBody CommentRequest data, HttpServletRequest request) {
        return createProjectManager(projectId, request).comment(data);
    }

    @RequestMapping(value = "/comments/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public List<Comment> commentsDelete(@PathVariable("projectId") Long projectId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        return createProjectManager(projectId, request).deleteComment(commentId);
    }

}
