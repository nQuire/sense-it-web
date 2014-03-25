package org.greengin.senseitweb.controllers.activities.challenge;


import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeActivity;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeOutcome;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.permissions.SubscriptionManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.logic.persistence.EntityManagerFactory;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeOutcomeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge/outcome")
public class ChallengeOutcomeController extends AbstractChallengeController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		return createManager(projectId, request).getOutcome();
	}

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public ChallengeOutcome select(@PathVariable("projectId") Long projectId, ChallengeOutcomeRequest outcomeData, HttpServletRequest request) {
        return createManager(projectId, request).updateOutcome(outcomeData);
	}
	
}
