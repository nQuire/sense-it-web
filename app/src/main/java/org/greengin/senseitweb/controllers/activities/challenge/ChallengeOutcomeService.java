package org.greengin.senseitweb.controllers.activities.challenge;


import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeOutcomeRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping(value = "/project/{projectId}/challenge/outcome")
public class ChallengeOutcomeService {


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		ChallengeActivityActions participant = new ChallengeActivityActions(projectId, request);
		return participant.getOutcome();
	}

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome select(@PathVariable("projectId") Long projectId, ChallengeOutcomeRequest outcomeData, HttpServletRequest request) {
		ChallengeActivityActions admin = new ChallengeActivityActions(projectId, request);
		return admin.updateOutcome(outcomeData);
	}
	
}
