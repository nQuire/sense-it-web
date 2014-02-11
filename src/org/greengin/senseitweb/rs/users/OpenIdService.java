package org.greengin.senseitweb.rs.users;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.persistence.EMF;

@Path("/openid")
public class OpenIdService {

	static final String OPENID_QUERY = String.format("SELECT p FROM %s p JOIN p.openIds i WHERE i.openId = :oid",
			UserProfile.class.getName());

	@Path("/logout")
	@GET
	@Produces("application/json")
	public StatusResponse logout(@PathParam("provider") String provider, @Context HttpServletRequest request) {
		OpenId.instance().logout(request);

		StatusResponse response = new StatusResponse();
		response.setLogged(false);
		response.setNewUser(false);
		response.setProfile(null);

		return response;
	}

	@Path("/status")
	@GET
	@Produces("application/json")
	public StatusResponse status(@PathParam("provider") String provider, @Context HttpServletRequest request) {
		String id = OpenId.instance().getId(request);

		StatusResponse response = new StatusResponse();
		if (id == null) {
			response.setLogged(false);
			response.setNewUser(false);
			response.setProfile(null);
		} else {
			response.setLogged(true);

			EntityManager em = EMF.get().createEntityManager();
			Query query = em.createQuery(OPENID_QUERY);
			query.setParameter("oid", id);
			List<?> profiles = query.getResultList();

			if (profiles.size() == 0) {
				em.getTransaction().begin();
				UserProfile profile = new UserProfile();
				profile.setName("");
				Collection<OpenIdEntity> ids = new Vector<OpenIdEntity>();

				OpenIdEntity openid = new OpenIdEntity();
				openid.setOpenId(id);
				openid.setEmail(OpenId.instance().getEmail(request));
				ids.add(openid);

				profile.setOpenIds(ids);
				em.persist(profile);

				em.getTransaction().commit();

				response.setProfile(profile);
				response.setNewUser(true);
			} else {
				response.setProfile((UserProfile) profiles.get(0));
				response.setNewUser(false);
			}
		}

		return response;
	}
}
