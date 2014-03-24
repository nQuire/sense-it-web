package org.greengin.senseitweb.controllers.activities.senseit;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@RequestMapping(value = "/project/{projectId}/senseit/data")
public class SenseItDataController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public Collection<SenseItSeries> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        SenseItActivityActions member = new SenseItActivityActions(projectId, request);
        return member.getData();
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public NewDataItemResponse<SenseItSeries> upload(@PathVariable("projectId") Long projectId,
                                                     @RequestParam("title") String title,
                                                     @RequestParam("geolocation") String geolocation,
                                                     @RequestParam("file") MultipartFile file,
                                                     HttpServletRequest request) {

        SenseItActivityActions member = new SenseItActivityActions(projectId, request);
        try {
            return member.createData(new SenseItSeriesManipulator(title, geolocation, file.getInputStream()));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long delete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        SenseItActivityActions member = new SenseItActivityActions(projectId, request);
        return member.deleteData(dataId, new SenseItSeriesManipulator(null, null, null));
    }

    @RequestMapping(value = "/vote/{dataId}", method = RequestMethod.POST)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long itemId, VoteRequest voteData, HttpServletRequest request) {
        SenseItActivityActions voter = new SenseItActivityActions(projectId, request);
        return voter.voteItem(itemId, voteData);
    }

    @RequestMapping(value = "/{dataId}/{varId}.png", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] plot(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @PathVariable("varId") String varId, HttpServletRequest request) {
        SenseItActivityActions member = new SenseItActivityActions(projectId, request);
        return member.getPlot(dataId, varId);
    }


}
