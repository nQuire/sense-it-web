package org.greengin.senseitweb.rs.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.persistence.EMF;

@Path("/openid")
public class OpenIdService {

	static final String OPENID_QUERY = String.format("SELECT p FROM %s p JOIN p.openIds i WHERE i.openId = :oid",
			UserProfile.class.getName());

	@Path("/logout")
	@GET
	@Produces("application/json")
	public StatusResponse logout(@PathParam("provider") String provider, @Context HttpServletRequest request) {
		OpenIdManager.instance().logout(request);

		StatusResponse response = new StatusResponse();
		response.setLogged(false);
		response.setProfile(null);

		return response;
	}

	@Path("/profile")
	@GET
	@Produces("application/json")
	public StatusResponse status(@Context HttpServletRequest request) {
		UserProfile profile = UsersManager.get().currentUser(request);
		StatusResponse response = new StatusResponse();
		response.setLogged(profile != null);
		response.setProfile(profile);
		return response;
	}
	
	@Path("/profile")
	@PUT
	@Produces("application/json")
	public Boolean update(@Context HttpServletRequest request, ProfileRequest profileData) {
		String id = OpenIdManager.instance().getId(request);

		if (id != null) {
			EntityManager em = EMF.get().createEntityManager();
			TypedQuery<UserProfile> query = em.createQuery(OPENID_QUERY, UserProfile.class);
			query.setParameter("oid", id);
			List<UserProfile> profiles = query.getResultList();

			if (profiles.size() > 0) {
				
				em.getTransaction().begin();
				UserProfile profile = profiles.get(0);
				profile.setName(profileData.getName());
				em.getTransaction().commit();
				
				return true;
			} 
		}

		return false;
	}
}
