package org.greengin.senseitweb.rs.activities.senseit;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.map.annotate.JsonView;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.project.senseit.JoinedProfilesActions;
import org.greengin.senseitweb.logic.project.senseit.JoinedProfilesResponse;

@Path("/senseit/profiles")
public class SenseItProfilesService {
	
	@GET
	@Produces("application/json")
	@JsonView({Views.None.class})
	public JoinedProfilesResponse get(@Context HttpServletRequest request) {
		JoinedProfilesActions member = new JoinedProfilesActions(request);
		return member.joinedProfiles();
	}
}
