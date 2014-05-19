package org.greengin.nquireit.controllers.activities.spotit;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.controllers.activities.senseit.AbstractSenseItController;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.project.spotit.JoinedProjectResponse;
import org.greengin.nquireit.logic.project.spotit.JoinedProjectsActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/api/spotit/projects")
public class SpotItProjectsController extends AbstractSenseItController {

    @Autowired
    ContextBean context;

    @RequestMapping(method = RequestMethod.GET)
	@ResponseView(Views.VotableCount.class)
    @ResponseBody
	public HashMap<Long, JoinedProjectResponse> get(HttpServletRequest request) {
		return new JoinedProjectsActions(context, request).joinedProjects();
	}

}
