package org.greengin.senseitweb.controllers.activities.senseit;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItAnalysisManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping(value = "/project/{projectId}/senseit/analysys")
public class SenseItAnalysisController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public Collection<SenseItAnalysis> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.getAnalysis();
	}


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public NewDataItemResponse<SenseItAnalysis> create(@PathVariable("projectId") Long projectId, SenseItAnalysisManipulator manipulator, HttpServletRequest request) {
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.createAnalysis(manipulator);
	}

    @RequestMapping(value = "/{analysisId}", method = RequestMethod.PUT)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public SenseItAnalysis update(@PathVariable("projectId") Long projectId, @PathVariable("analysisId") Long analysisId, SenseItAnalysisManipulator manipulator, HttpServletRequest request) {
		SenseItActivityActions member = new SenseItActivityActions(projectId, request);
		return member.updateAnalysis(analysisId, manipulator);
	}

    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    @ResponseBody
	public Boolean delete(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		//SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return false;
	}

    @RequestMapping(value = "/vote/{itemId}", method = RequestMethod.POST)
    @ResponseBody
	@JsonView({Views.VotableCount.class})
	public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("itemId") Long itemId, VoteRequest voteData, HttpServletRequest request) {
		SenseItActivityActions voter = new SenseItActivityActions(projectId, request);
		return voter.voteItem(itemId, voteData);
	}
}
