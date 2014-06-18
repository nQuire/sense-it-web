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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ProjectListResponse projectList(HttpServletRequest request) {
        return createProjectManager(request).getProjects();
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
