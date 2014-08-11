package org.greengin.nquireit.controllers.admin;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.projects.Project;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.admin.AdminActions;
import org.greengin.nquireit.logic.admin.ProjectFeaturedRequest;
import org.greengin.nquireit.logic.admin.UserAdminRequest;
import org.greengin.nquireit.logic.base.TextRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    ContextBean contextBean;

    private AdminActions createAdminManager(HttpServletRequest request) {
        return new AdminActions(contextBean, request);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.ForumList.class)
    public List<UserProfile> users(HttpServletRequest request) {
        return createAdminManager(request).getUsers();
    }

    @RequestMapping(value = "/user/{userId}/admin", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public List<UserProfile> setAdmin(@PathVariable("userId") Long userId, @RequestBody UserAdminRequest data, HttpServletRequest request) {
        AdminActions manager = createAdminManager(request);
        manager.setAdmin(userId, data);
        return manager.getUsers();
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserName.class)
    public List<Project> projects(HttpServletRequest request) {
        return createAdminManager(request).getProjects();
    }


    @RequestMapping(value = "/project/{projectId}/featured", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public List<Project> setFeatured(@PathVariable("projectId") Long projectId, @RequestBody ProjectFeaturedRequest data, HttpServletRequest request) {
        AdminActions manager = createAdminManager(request);
        manager.setFeatured(projectId, data);
        return manager.getProjects();
    }

    @RequestMapping(value = "/text", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public Boolean setFeatured(@RequestBody TextRequest data, HttpServletRequest request) {
        AdminActions manager = createAdminManager(request);
        return manager.setText(data.getKey(), data.getContent());
   }

    @RequestMapping(value = "/model/update", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.UserProfileData.class)
    public Boolean modelUpdate(HttpServletRequest request) {
        AdminActions manager = createAdminManager(request);
        return manager.updateModel();
    }


}
