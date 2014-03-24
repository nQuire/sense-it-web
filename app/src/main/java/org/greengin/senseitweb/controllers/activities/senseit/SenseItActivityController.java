package org.greengin.senseitweb.controllers.activities.senseit;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.senseit.SenseItActivityActions;
import org.greengin.senseitweb.logic.project.senseit.SensorInputRequest;
import org.greengin.senseitweb.logic.project.senseit.SenseItProfileRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/project/{projectId}/senseit")
public class SenseItActivityController {

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
	public Project updateProfile(@PathVariable("projectId") Long projectId, SenseItProfileRequest profileData, HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.updateProfile(profileData);
	}

    @RequestMapping(value = "/inputs", method = RequestMethod.POST)
    @ResponseBody
	public Project create(@PathVariable("projectId") Long projectId, SensorInputRequest inputData, HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.createSensor(inputData);
	}

    @RequestMapping(value = "/input/{inputId}", method = RequestMethod.PUT)
    @ResponseBody
	public Project update(@PathVariable("projectId") Long projectId, @PathVariable("inputId") Long inputId, SensorInputRequest inputData, HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.updateSensor(inputId, inputData);
	}

    @RequestMapping(value = "/input/{inputId}", method = RequestMethod.DELETE)
    @ResponseBody
	public Project delete(@PathVariable("projectId") Long projectId, @PathVariable("inputId") Long inputId, HttpServletRequest request) {
		SenseItActivityActions editor = new SenseItActivityActions(projectId, request);
		return editor.deleteSensor(inputId);
	}

}
