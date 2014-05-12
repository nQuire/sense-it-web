package org.greengin.nquireit.controllers.projects;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/api/projects")
public class SimpleProjectListController {

    @Autowired
    ContextBean context;


    private ProjectActions createProjectManager(HttpServletRequest request, Long projectId) {
        return new ProjectActions(context, projectId, request);
    }

    private List<SimpleProjectResponse> membershipAction(HttpServletRequest request, Long projectId, String type, boolean join) {
        ProjectActions actions = createProjectManager(request, projectId);
        if (join) {
            actions.join();
        } else {
            actions.leave();
        }

        return actions.getProjectsSimple(type);
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(Views.VotableCount.class)
    public List<SimpleProjectResponse> projectList(@PathVariable("type") String type, HttpServletRequest request) {
        return createProjectManager(request, null).getProjectsSimple(type);
    }

    @RequestMapping(value = "/{type}/{projectId}/join", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(Views.VotableCount.class)
    public List<SimpleProjectResponse> join(@PathVariable("type") String type,
                                                 @PathVariable("projectId") Long projectId,
                                                 HttpServletRequest request) {
        return membershipAction(request, projectId, type, true);
    }

    @RequestMapping(value = "/{type}/{projectId}/leave", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(Views.VotableCount.class)
    public List<SimpleProjectResponse> leave(@PathVariable("type") String type,
                                                 @PathVariable("projectId") Long projectId,
                                                 HttpServletRequest request) {
        return membershipAction(request, projectId, type, false);
    }

}
