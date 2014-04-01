package org.greengin.senseitweb.controllers.activities.challenge;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeFieldRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/api/project/{projectId}/challenge")
public class ChallengeActivityController extends AbstractChallengeController {


    @RequestMapping(method = RequestMethod.PUT)
    public Project updateActivity(@PathVariable("projectId") Long projectId, @RequestBody ChallengeActivityRequest activityData, HttpServletRequest request) {
        return createManager(projectId, request).updateActivity(activityData);
    }

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    @ResponseBody
    public Project create(@PathVariable("projectId") Long projectId, @RequestBody ChallengeFieldRequest fieldData, HttpServletRequest request) {
        return createManager(projectId, request).createField(fieldData);
    }

    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.PUT)
    @ResponseBody
    public Project update(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, @RequestBody ChallengeFieldRequest fieldData, HttpServletRequest request) {
        return createManager(projectId, request).updateField(fieldId, fieldData);
    }

    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Project delete(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, HttpServletRequest request) {
        return createManager(projectId, request).deleteField(fieldId);
    }

}
