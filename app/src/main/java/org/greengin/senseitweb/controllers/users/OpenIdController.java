package org.greengin.senseitweb.controllers.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonView;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.mixins.Views;
import org.greengin.senseitweb.logic.permissions.OpenIdManager;
import org.greengin.senseitweb.logic.permissions.UsersManager;
import org.greengin.senseitweb.persistence.EMF;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/openid")
public class OpenIdController {

	static final String OPENID_QUERY = String.format("SELECT p FROM %s p JOIN p.openIds i WHERE i.openId = :oid",
			UserProfile.class.getName());

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.UserOpenIds.class})
	public StatusResponse logout(@PathVariable("provider") String provider, HttpServletRequest request) {
		OpenIdManager.instance().logout(request);

		StatusResponse response = new StatusResponse();
		response.setLogged(false);
		response.setProfile(null);

		return response;
	}

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
	@JsonView({Views.UserOpenIds.class})
	public StatusResponse status(HttpServletRequest request) {
		UserProfile profile = UsersManager.get().currentUser(request);
		StatusResponse response = new StatusResponse();
		response.setLogged(profile != null);
		response.setProfile(profile);
		response.setToken(UsersManager.get().userToken(request));
		return response;
	}

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
	public Boolean update(ProfileRequest profileData, HttpServletRequest request) {
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
