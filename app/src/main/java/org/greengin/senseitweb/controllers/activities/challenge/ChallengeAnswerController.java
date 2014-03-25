package org.greengin.senseitweb.controllers.activities.challenge;

import  java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeAnswerRequest;
import org.greengin.senseitweb.logic.project.challenge.NewChallengeAnswerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/project/{projectId}/challenge/answers")
public class ChallengeAnswerController extends AbstractChallengeController{


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.User.class})
	public Collection<ChallengeAnswer> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getAnswersForParticipant();
	}

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
	public NewChallengeAnswerResponse create(@PathVariable("projectId") Long projectId, ChallengeAnswerRequest answerData, HttpServletRequest request) {
        return createManager(projectId, request).createAnswer(answerData);
	}
	
	@RequestMapping(value = "/{answerId}", method = RequestMethod.PUT)
    @ResponseBody
	public Collection<ChallengeAnswer> update(@PathVariable("projectId") Long projectId, @PathVariable("answerId") Long answerId, ChallengeAnswerRequest answerData, HttpServletRequest request) {
        return createManager(projectId, request).updateAnswer(answerId, answerData);
	}

	@RequestMapping(value = "/{answerId}", method = RequestMethod.DELETE)
    @ResponseBody
	public Collection<ChallengeAnswer> delete(@PathVariable("projectId") Long projectId, @PathVariable("answerId") Long answerId, HttpServletRequest request) {
        return createManager(projectId, request).deleteAnswer(answerId);
	}

}
