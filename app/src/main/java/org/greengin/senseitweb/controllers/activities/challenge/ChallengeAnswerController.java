package org.greengin.senseitweb.controllers.activities.challenge;

import  java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.senseitweb.logic.project.challenge.NewChallengeAnswerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge/answers")
public class ChallengeAnswerController extends AbstractChallengeController{


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.User.class)
	public Collection<ChallengeAnswer> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getAnswersForParticipant();
	}

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
	public NewChallengeAnswerResponse create(@PathVariable("projectId") Long projectId, @RequestBody ChallengeAnswerRequest answerData, HttpServletRequest request) {
        return createManager(projectId, request).createAnswer(answerData);
	}
	
	@RequestMapping(value = "/{answerId}", method = RequestMethod.PUT)
    @ResponseBody
	public Collection<ChallengeAnswer> update(@PathVariable("projectId") Long projectId, @PathVariable("answerId") Long answerId, @RequestBody ChallengeAnswerRequest answerData, HttpServletRequest request) {
        return createManager(projectId, request).updateAnswer(answerId, answerData);
	}

	@RequestMapping(value = "/{answerId}", method = RequestMethod.DELETE)
    @ResponseBody
	public Collection<ChallengeAnswer> delete(@PathVariable("projectId") Long projectId, @PathVariable("answerId") Long answerId, HttpServletRequest request) {
        return createManager(projectId, request).deleteAnswer(answerId);
	}

}
