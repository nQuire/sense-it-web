package org.greengin.nquireit.controllers.activities.senseit;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.nquireit.entities.activities.senseit.SenseItSeries;
import org.greengin.nquireit.entities.rating.Comment;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.data.NewDataItemResponse;
import org.greengin.nquireit.logic.project.senseit.SenseItSeriesManipulator;
import org.greengin.nquireit.logic.project.senseit.UpdateTitleRequest;
import org.greengin.nquireit.logic.rating.CommentRequest;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.greengin.nquireit.utils.TimeValue;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/senseit/data")
public class SenseItDataController extends AbstractSenseItController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.VotableCount.class)
    public Collection<SenseItSeries> get(@PathVariable("projectId") Long projectId, HttpServletRequest request) {
        return createManager(projectId, request).getData();
    }

    @RequestMapping(value = "/csv", method = RequestMethod.GET)
    public void csv(@PathVariable("projectId") Long projectId, HttpServletRequest request, HttpServletResponse response) {
        Collection<SenseItSeries> series = createManager(projectId, request).getData();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%d.csv\"", projectId));

        try {
            PrintWriter pw = new PrintWriter(response.getOutputStream());
            Iterator<SenseItSeries> i = series.iterator();
            Comparator<Map.Entry<Long, String>> sensorComparator = new Comparator<Map.Entry<Long, String>>() {
                @Override
                public int compare(Map.Entry<Long, String> o1, Map.Entry<Long, String> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            };
            Comparator<Map.Entry<String, TimeValue>> valuesComparator = new Comparator<Map.Entry<String, TimeValue>>() {
                @Override
                public int compare(Map.Entry<String, TimeValue> o1, Map.Entry<String, TimeValue> o2) {
                    return o1.getKey().compareTo(o2.getKey());
                }
            };

            while (i.hasNext()) {
                SenseItSeries s = i.next();
                pw.print(s.getTitle());

                pw.print(",");
                pw.print(s.getAuthor().getUsername());

                pw.print(",");
                pw.print(s.getDate());

                List<Map.Entry<Long, String>> sensors = new ArrayList<Map.Entry<Long, String>>(s.getSensors().entrySet());
                Collections.sort(sensors, sensorComparator);
                for (Map.Entry<Long, String> entry : sensors) {
                    pw.print(",");
                    pw.print(entry.getValue());
                }

                List<Map.Entry<String, TimeValue>> values = new ArrayList<Map.Entry<String, TimeValue>>(s.getVarValue().entrySet());
                Collections.sort(values, valuesComparator);
                for (Map.Entry<String, TimeValue> entry : values) {
                    pw.print(",");
                    if (entry.getValue().getV().length > 0) {
                        pw.print(entry.getValue().getV()[0]);
                    }
                }

                pw.println();
            }

            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

            return createManager(projectId, request).createData(new SenseItSeriesManipulator(title, geolocation, file.getInputStream(), null));
        } catch (IOException e) {
            return null;
        }
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Long delete(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, HttpServletRequest request) {
        return createManager(projectId, request).deleteData(dataId);
    }

    @RequestMapping(value = "/{dataId}", method = RequestMethod.PUT)
    @ResponseBody
    public SenseItSeries update(@PathVariable("projectId") Long projectId, @PathVariable("dataId") Long dataId, @RequestBody UpdateTitleRequest data, HttpServletRequest request) {
        return createManager(projectId, request).updateData(dataId, new SenseItSeriesManipulator(null, null, null, data));
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
