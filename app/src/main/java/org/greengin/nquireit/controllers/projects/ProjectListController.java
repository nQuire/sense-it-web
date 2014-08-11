package org.greengin.nquireit.controllers.projects;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.MyProjectListResponse;
import org.greengin.nquireit.logic.project.ProjectActions;
import org.greengin.nquireit.logic.project.ProjectListResponse;
import org.greengin.nquireit.logic.project.ProjectCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/projects")
public class ProjectListController {


    @Autowired
    ContextBean context;


    private ProjectActions createProjectManager(HttpServletRequest request) {
        return new ProjectActions(context, null, request);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(Views.UserName.class)
    public ProjectListResponse projectList(@RequestParam(value = "type", defaultValue = "") String type, @RequestParam(value = "status", defaultValue = "") String status, @RequestParam(value = "filter", defaultValue = "") String filter, @RequestParam(value = "kw", defaultValue = "") String keyword, HttpServletRequest request) {
        return createProjectManager(request).getProjects(type, status, filter, keyword);
    }

    @RequestMapping(value = "/mine", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(Views.UserName.class)
    public MyProjectListResponse myProjectList(HttpServletRequest request) {
        return createProjectManager(request).getMyProjects();
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Long create(@RequestBody ProjectCreationRequest projectData, HttpServletRequest request) {
        return createProjectManager(request).createProject(projectData);
    }

}
