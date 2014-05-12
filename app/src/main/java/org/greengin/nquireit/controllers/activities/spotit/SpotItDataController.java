package org.greengin.nquireit.controllers.activities.spotit;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.controllers.activities.senseit.AbstractSenseItController;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.json.JacksonObjectMapper;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.NewDataItemResponse;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.files.FileUtils;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.logic.project.senseit.SenseItActivityActions;
import org.greengin.nquireit.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.nquireit.logic.project.spotit.SpotItActivityActions;
import org.greengin.nquireit.logic.project.spotit.SpotItObservationManipulator;
import org.greengin.nquireit.logic.project.spotit.SpotItObservationRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Controller
@RequestMapping(value = "/api/project/{projectId}/spotit/data")
public class SpotItDataController {

    @Autowired
    ContextBean context;

    @Autowired
    JacksonObjectMapper objectMapper;

    protected SpotItActivityActions createManager(Long projectId, HttpServletRequest request) {
        return new SpotItActivityActions(context, projectId, request);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public Collection<SpotItObservation> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getData();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public NewDataItemResponse<SpotItObservation> upload(@PathVariable("projectId") Long projectId,
                                                     HttpServletRequest request) {
        try {
            SpotItObservationRequest requestData = FileUtils.readParam(request, objectMapper, "body", SpotItObservationRequest.class);
            FileMapUpload.FileData file = FileUtils.getFile(request, "observation");
            return createManager(projectId, request).createData(new SpotItObservationManipulator(context, requestData, file));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long delete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        return createManager(projectId, request).deleteData(dataId, new SpotItObservationManipulator(context, null, null));
    }

    @RequestMapping(value = "/vote/{dataId}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long itemId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteItem(itemId, voteData);
    }

}
