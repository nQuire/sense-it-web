package org.greengin.senseitweb.controllers.activities.senseit;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItAnalysis;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItAnalysisManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/senseit/analysis")
public class SenseItAnalysisController extends AbstractSenseItController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public Collection<SenseItAnalysis> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getAnalysis();
	}


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public NewDataItemResponse<SenseItAnalysis> create(@PathVariable("projectId") Long projectId, @RequestBody SenseItAnalysisManipulator manipulator, HttpServletRequest request) {
        return createManager(projectId, request).createAnalysis(manipulator);
	}

    @RequestMapping(value = "/{analysisId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public SenseItAnalysis update(@PathVariable("projectId") Long projectId, @PathVariable("analysisId") Long analysisId, @RequestBody SenseItAnalysisManipulator manipulator, HttpServletRequest request) {
        return createManager(projectId, request).updateAnalysis(analysisId, manipulator);
	}

    @RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
    @ResponseBody
	public Boolean delete(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
		//SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return false;
	}

    @RequestMapping(value = "/vote/{itemId}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("itemId") Long itemId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteItem(itemId, voteData);
	}
}
