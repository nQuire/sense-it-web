package org.greengin.senseitweb.rs.users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.users.UserProfile;


@Path("/user")
public class UsersService {

	static final String QUERY_BY_USERID = String.format("SELECT u FROM %s u WHERE u.userId = :id", UserProfile.class.getName());
	
	private UserProfile getUserProfile(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			Object value = session.getAttribute("userid");
			if (value != null && value instanceof UserProfile) {
				return (UserProfile) value;
			}
		}
		
		return null;		
	}
	
	
	@GET
	@Produces("application/json")
	public UserResponse get(@Context HttpServletRequest request) {
		UserResponse response = new UserResponse();
		UserProfile profile = getUserProfile(request);
		
		if (profile != null) {
			response.setNewUser(false);
			response.setLogged(true);
			response.setProfile(profile);
			response.setUrl("/");
		} else {
			response.setLogged(false);
			response.setProfile(null);
			response.setNewUser(false);
			response.setUrl("/");
		}

		return response;
	}
}
