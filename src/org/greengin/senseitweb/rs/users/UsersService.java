package org.greengin.senseitweb.rs.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.persistence.EMF;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Path("/user")
public class UsersService {

	static final String QUERY_BY_USERID = String.format("SELECT u FROM %s u WHERE u.userId = :id", UserProfile.class.getName());

	@GET
	@Produces("application/json")
	public UserResponse get() {
		UserResponse response = new UserResponse();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			UserProfile profile;
			
			EntityManager em = EMF.get().createEntityManager();
			List<?> profiles = em.createQuery(QUERY_BY_USERID).setParameter("id", user.getUserId()).getResultList();
			
			boolean newUser = profiles.isEmpty();
			
			if (newUser) {
				profile = new UserProfile();
				profile.setUserId(user.getUserId());
				profile.setName("");
				em.persist(profile);
				em.refresh(profile);
			} else {
				profile = (UserProfile) profiles.get(0);

				if (profiles.size() > 1) {
					em.getTransaction().begin();
					for (int i = 1; i < profiles.size(); i++) {
						em.remove(profiles.get(i));
					}
					em.getTransaction().commit();
				}
			}
			
			response.setNewUser(newUser);
			response.setLogged(true);
			response.setProfile(profile);
			response.setEmail(user.getEmail());
			response.setUrl(userService.createLogoutURL("/"));
		} else {
			response.setLogged(false);
			response.setProfile(null);
			response.setNewUser(false);
			response.setUrl(userService.createLoginURL("/"));
		}

		return response;
	}
}
