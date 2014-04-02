package org.greengin.senseitweb.controllers.activities.senseit;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.activities.senseit.SenseItSeries;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.data.NewDataItemResponse;
import org.greengin.senseitweb.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.senseitweb.logic.voting.VoteCount;
import org.greengin.senseitweb.logic.voting.VoteRequest;
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
@RequestMapping(value = "/api/project/{projectId}/senseit/data")
public class SenseItDataController extends AbstractSenseItController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public Collection<SenseItSeries> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getData();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public NewDataItemResponse<SenseItSeries> upload(@PathVariable("projectId") Long projectId,
                                                     HttpServletRequest request) {
        try {
            DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) request;
            String title = multiPartRequest.getParameter("title");
            String geolocation = multiPartRequest.getParameter("geolocation");
            MultipartFile file = multiPartRequest.getFile("file");

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
    @ResponseView(value = Views.VotableCount.class)
    public VoteCount vote(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long itemId, @RequestBody VoteRequest voteData, HttpServletRequest request) {
        return createManager(projectId, request).voteItem(itemId, voteData);
    }

    @RequestMapping(value = "/{dataId}/{varId}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public void plot(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @PathVariable("varId") String varId, HttpServletRequest request, HttpServletResponse response) {
        byte[] image = createManager(projectId, request).getPlot(dataId, varId);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        try {
            response.getOutputStream().write(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
