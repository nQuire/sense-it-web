package org.greengin.nquireit.controllers.activities.challenge;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.activities.challenge.ChallengeAnswer;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.project.challenge.ChallengeActivityActions;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
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
