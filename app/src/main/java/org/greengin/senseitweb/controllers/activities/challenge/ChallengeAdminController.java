package org.greengin.senseitweb.controllers.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeStageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge/admin")
public class ChallengeAdminController extends AbstractChallengeController {


    @RequestMapping(value = "/stage", method = RequestMethod.PUT)
    @ResponseBody
	public Project setStage(@PathVariable("projectId") Long projectId, @RequestBody ChallengeStageRequest stage, HttpServletRequest request) {
        return createManager(projectId, request).setStage(stage.getStage());
	}

    @RequestMapping(value = "/answers", method = RequestMethod.GET)
    @ResponseBody
	@ResponseView(value = Views.VotableCountModeration.class)
	public Collection<ChallengeAnswer> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getAnswersForAdmin();
	}
}
