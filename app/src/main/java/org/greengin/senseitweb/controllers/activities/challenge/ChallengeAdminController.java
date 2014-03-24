package org.greengin.senseitweb.controllers.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeStageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/project/{projectId}/challenge/admin")
public class ChallengeAdminController {


    @RequestMapping(value = "/stage", method = RequestMethod.PUT)
    @ResponseBody
	public Project setStage(@PathVariable("projectId") Long projectId, ChallengeStageRequest stage, HttpServletRequest request) {
		ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
		return manager.setStage(stage.getStage());
	}

    @RequestMapping(value = "/answers", method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.VotableCountModeration.class})
	public Collection<ChallengeAnswer> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		ChallengeActivityActions participant = new ChallengeActivityActions(projectId, request);
		return participant.getAnswersForAdmin();
	}
}
