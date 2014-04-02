package org.greengin.senseitweb.controllers.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.activities.challenge.ChallengeAnswer;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge/votes")
public class ChallengeVoteController extends AbstractChallengeController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public Collection<ChallengeAnswer> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		ChallengeActivityActions participant = createManager(projectId, request);

		Collection<ChallengeAnswer> answers = participant.getAnswersForParticipant();
		for (ChallengeAnswer a : answers) {
			a.setSelectedVoteAuthor(participant.getCurrentUser());
		}
		return answers;
	}
	
	
    @RequestMapping(value = "/{answerId}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("answerId") Long answerId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
		ChallengeActivityActions voter = createManager(projectId, request);
		return voter.vote(answerId, voteData);
	}
}
