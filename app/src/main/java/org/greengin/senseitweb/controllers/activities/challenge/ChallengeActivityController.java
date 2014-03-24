package org.greengin.senseitweb.controllers.activities.challenge;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityActions;
import org.greengin.senseitweb.logic.project.challenge.ChallengeActivityRequest;
import org.greengin.senseitweb.logic.project.challenge.ChallengeFieldRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/project/{projectId}/challenge")
public class ChallengeActivityController {

    @RequestMapping(method = RequestMethod.PUT)
    public Project updateActivity(@PathVariable("projectId") Long projectId, ChallengeActivityRequest activityData, HttpServletRequest request) {
        ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
        return manager.updateActivity(activityData);
    }

    @RequestMapping(value = "/fields", method = RequestMethod.POST)
    @ResponseBody
    public Project create(@PathVariable("projectId") Long projectId, ChallengeFieldRequest fieldData, HttpServletRequest request) {
        ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
        return manager.createField(fieldData);
    }

    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.PUT)
    @ResponseBody
    public Project update(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, ChallengeFieldRequest fieldData, HttpServletRequest request) {
        ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
        return manager.updateField(fieldId, fieldData);
    }

    @RequestMapping(value = "/field/{fieldId}", method = RequestMethod.DELETE)
    @ResponseBody
    public Project delete(@PathVariable("projectId") Long projectId, @PathVariable("fieldId") Long fieldId, HttpServletRequest request) {
        ChallengeActivityActions manager = new ChallengeActivityActions(projectId, request);
        return manager.deleteField(fieldId);
    }

}
