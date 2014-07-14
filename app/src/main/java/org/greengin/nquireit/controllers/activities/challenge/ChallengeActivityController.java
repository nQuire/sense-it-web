package org.greengin.nquireit.controllers.activities.challenge;

import org.greengin.nquireit.logic.project.ProjectResponse;
import org.greengin.nquireit.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.nquireit.logic.project.challenge.ChallengeFieldMoveRequest;
import org.greengin.nquireit.logic.project.challenge.ChallengeFieldRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge")
public class ChallengeActivityController extends AbstractChallengeController {


    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ProjectResponse updateActivity(@PathVariable("projectId") Long projectId, @RequestBody ChallengeActivityRequest activityData, HttpServletRequest request) {
        return createManager(projectId, request).updateActivity(activityData);
    }

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    @ResponseBody
    public ProjectResponse create(@PathVariable("projectId") Long projectId, @RequestBody ChallengeFieldRequest fieldData, HttpServletRequest request) {
        return createManager(projectId, request).createField(fieldData);
    }




    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.PUT)
    @ResponseBody
    public ProjectResponse update(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, @RequestBody ChallengeFieldRequest fieldData, HttpServletRequest request) {
        return createManager(projectId, request).updateField(fieldId, fieldData);
    }

     @RequestMapping(value = "/field/{fieldId}/move", method = RequestMethod.POST)
    @ResponseBody
    public ProjectResponse update(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, @RequestBody ChallengeFieldMoveRequest fieldData, HttpServletRequest request) {
        return createManager(projectId, request).moveField(fieldId, fieldData);
    }



    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ProjectResponse delete(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, HttpServletRequest request) {
        return createManager(projectId, request).deleteField(fieldId);
    }

}
