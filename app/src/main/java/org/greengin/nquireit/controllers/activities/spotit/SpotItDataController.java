package org.greengin.nquireit.controllers.activities.spotit;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.activities.spotit.SpotItObservation;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.json.JacksonObjectMapper;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.data.NewDataItemResponse;
import org.greengin.nquireit.logic.files.FileMapUpload;
import org.greengin.nquireit.logic.files.RequestsUtils;
import org.greengin.nquireit.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.nquireit.logic.project.senseit.UpdateTitleRequest;
import org.greengin.nquireit.logic.project.spotit.SpotItActivityActions;
import org.greengin.nquireit.logic.project.spotit.SpotItObservationManipulator;
import org.greengin.nquireit.logic.project.spotit.SpotItObservationRequest;
import org.greengin.nquireit.logic.project.spotit.UpdateImageRequest;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
            DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
            SpotItObservationRequest requestData = new SpotItObservationRequest();
            requestData.setTitle(multiPartRequest.getParameter("title"));
            requestData.setGeolocation(multiPartRequest.getParameter("geolocation"));
            requestData.setDate(RequestsUtils.getLong(multiPartRequest, "date", (new Date()).getTime()));

            FileMapUpload.FileData file = RequestsUtils.getFile(request, "image");

            return createManager(projectId, request).createData(new SpotItObservationManipulator(context, requestData, file, null));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.PUT)
    @ResponseBody
    public SpotItObservation update(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @RequestBody UpdateImageRequest data, HttpServletRequest request) {
        return createManager(projectId, request).updateData(dataId, new SpotItObservationManipulator(context, null, null, data));
    }

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public NewDataItemResponse<SpotItObservation> uploadFiles(@PathVariable("projectId") Long projectId,
                                                         HttpServletRequest request) {
        try {
            DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
            SpotItObservationRequest requestData = new SpotItObservationRequest();
            requestData.setTitle(multiPartRequest.getParameter("title"));
            requestData.setGeolocation(multiPartRequest.getParameter("geolocation"));
            requestData.setDate(RequestsUtils.getLong(multiPartRequest, "date", (new Date()).getTime()));

            FileMapUpload.FileData file = RequestsUtils.getFile(request, "image");

            return createManager(projectId, request).createData(new SpotItObservationManipulator(context, requestData, file, null));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long delete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        return createManager(projectId, request).deleteData(dataId);
    }

    @RequestMapping(value = "/vote/{dataId}", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long itemId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteItem(itemId, voteData);
    }


    @RequestMapping(value = "/{dataId}/comments", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public List<Comment> comments(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        return createManager(projectId, request).getDataComments(dataId);
    }

    @RequestMapping(value = "/{dataId}/comments", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public List<Comment> commentsPost(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @RequestBody CommentRequest data, HttpServletRequest request) {
        return createManager(projectId, request).commentData(dataId, data);
    }

    @RequestMapping(value = "/{dataId}/comments/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public List<Comment> commentsDelete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @PathVariable("commentId") Long commentId, HttpServletRequest request) {
        return createManager(projectId, request).deleteDataComment(dataId, commentId);
    }

    @RequestMapping(value = "/{dataId}/comments/{commentId}/vote", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public VoteCount commentsVote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @PathVariable("commentId") Long commentId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteDataComment(dataId, commentId, voteData);
    }
}
