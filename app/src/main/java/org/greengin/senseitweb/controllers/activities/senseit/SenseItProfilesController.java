package org.greengin.senseitweb.controllers.activities.senseit;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.senseit.JoinedProfilesActions;
import org.greengin.senseitweb.logic.project.senseit.JoinedProfilesResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value = "/senseit/profiles")
public class SenseItProfilesController {

    @RequestMapping(method = RequestMethod.GET)
	@JsonView({Views.None.class})
	public JoinedProfilesResponse get(HttpServletRequest request) {
		JoinedProfilesActions member = new JoinedProfilesActions(request);
		return member.joinedProfiles();
	}
}
