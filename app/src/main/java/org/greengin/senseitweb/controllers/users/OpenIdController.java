package org.greengin.senseitweb.controllers.users;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import com.mangofactory.jsonview.ResponseView;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.permissions.OpenIdManagerBean;
import org.greengin.senseitweb.logic.permissions.UsersManagerBean;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/openid")
public class OpenIdController {

	static final String OPENID_QUERY = String.format("SELECT p FROM %s p JOIN p.openIds i WHERE i.openId = :oid",
			UserProfile.class.getName());

    @Autowired
    OpenIdManagerBean openIdManager;

    @Autowired
    UsersManagerBean usersManager;

    @Autowired
    CustomEntityManagerFactoryBean entityManagerFactory;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserOpenIds.class)
	public StatusResponse logout(HttpServletRequest request) {
        openIdManager.logout(request);

		StatusResponse response = new StatusResponse();
		response.setLogged(false);
		response.setProfile(null);

		return response;
	}

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    @ResponseView(value = Views.UserOpenIds.class)
	public StatusResponse status(HttpServletRequest request) {
		UserProfile profile = usersManager.currentUser(request);
		StatusResponse response = new StatusResponse();
		response.setLogged(profile != null);
		response.setProfile(profile);
		response.setToken(usersManager.userToken(request));
		return response;
	}

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
	public Boolean update(@RequestBody ProfileRequest profileData, HttpServletRequest request) {
		String id = openIdManager.getId(request);

		if (id != null) {
			EntityManager em = entityManagerFactory.createEntityManager();
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
