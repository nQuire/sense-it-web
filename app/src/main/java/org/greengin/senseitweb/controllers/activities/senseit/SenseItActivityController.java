package org.greengin.senseitweb.controllers.activities.senseit;

import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.projects.Project;
import org.greengin.senseitweb.logic.project.ProjectResponse;
import org.greengin.senseitweb.logic.project.senseit.SensorInputRequest;
import org.greengin.senseitweb.logic.project.senseit.SenseItProfileRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/project/{projectId}/senseit")
public class SenseItActivityController extends AbstractSenseItController {


    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
	public ProjectResponse updateProfile(@PathVariable("projectId") Long projectId, @RequestBody SenseItProfileRequest profileData, HttpServletRequest request) {
		return createManager(projectId, request).updateProfile(profileData);
	}

    @RequestMapping(value = "/inputs", method = RequestMethod.POST)
    @ResponseBody
	public ProjectResponse create(@PathVariable("projectId") Long projectId, @RequestBody SensorInputRequest inputData, HttpServletRequest request) {
        return createManager(projectId, request).createSensor(inputData);
	}

    @RequestMapping(value = "/input/{inputId}", method = RequestMethod.PUT)
    @ResponseBody
	public ProjectResponse update(@PathVariable("projectId") Long projectId, @PathVariable("inputId") Long inputId, @RequestBody SensorInputRequest inputData, HttpServletRequest request) {
        return createManager(projectId, request).updateSensor(inputId, inputData);
	}

    @RequestMapping(value = "/input/{inputId}", method = RequestMethod.DELETE)
    @ResponseBody
	public ProjectResponse delete(@PathVariable("projectId") Long projectId, @PathVariable("inputId") Long inputId, HttpServletRequest request) {
        return createManager(projectId, request).deleteSensor(inputId);
	}

}
