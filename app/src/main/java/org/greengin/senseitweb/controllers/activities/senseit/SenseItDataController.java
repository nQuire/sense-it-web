package org.greengin.senseitweb.controllers.activities.senseit;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@Controller
@RequestMapping(value = "/api/project/{projectId}/senseit/data")
public class SenseItDataController extends AbstractSenseItController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public Collection<SenseItSeries> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getData();
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public NewDataItemResponse<SenseItSeries> upload(@PathVariable("projectId") Long projectId,
                                                     @RequestParam("title") String title,
                                                     @RequestParam("geolocation") String geolocation,
                                                     @RequestParam("file") MultipartFile file,
                                                     HttpServletRequest request) {
        try {
            return createManager(projectId, request).createData(new SenseItSeriesManipulator(title, geolocation, file.getInputStream()));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long delete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        return createManager(projectId, request).deleteData(dataId, new SenseItSeriesManipulator(null, null, null));
    }

    @RequestMapping(value = "/vote/{dataId}", method = RequestMethod.POST)
    @ResponseBody
    @JsonView({Views.VotableCount.class})
    public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long itemId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteItem(itemId, voteData);
    }

    @RequestMapping(value = "/{dataId}/{varId}.png", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] plot(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @PathVariable("varId") String varId, HttpServletRequest request) {
        return createManager(projectId, request).getPlot(dataId, varId);
    }


}
