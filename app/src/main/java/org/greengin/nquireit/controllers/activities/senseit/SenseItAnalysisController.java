package org.greengin.nquireit.controllers.activities.senseit;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.activities.base.BaseAnalysis;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.data.NewDataItemResponse;
import org.greengin.nquireit.logic.project.activity.BaseAnalysisManipulator;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/senseit/analysis")
public class SenseItAnalysisController extends AbstractSenseItController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public Collection<BaseAnalysis> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getAnalysis();
	}


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public NewDataItemResponse<BaseAnalysis> create(@PathVariable("projectId") Long projectId, @RequestBody BaseAnalysisManipulator manipulator, HttpServletRequest request) {
        return createManager(projectId, request).createAnalysis(manipulator);
	}

    @RequestMapping(value = "/{analysisId}", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
	public BaseAnalysis update(@PathVariable("projectId") Long projectId, @PathVariable("analysisId") Long analysisId, @RequestBody BaseAnalysisManipulator manipulator, HttpServletRequest request) {
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
