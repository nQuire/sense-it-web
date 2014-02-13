package org.greengin.senseitweb.logic.permissions;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.OpenIdEntity;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.persistence.EMF;

public class UsersManager {
	static final String OPENID_QUERY = String.format("SELECT p FROM %s p JOIN p.openIds i WHERE i.openId = :oid",
			UserProfile.class.getName());

	private static UsersManager um = new UsersManager();

	public static UsersManager get() {
		return um;
	}

	private UsersManager() {

	}

	public UserProfile currentUser(HttpServletRequest request) {

		String id = OpenIdManager.instance().getId(request);

		if (id != null) {
			EntityManager em = EMF.get().createEntityManager();
			TypedQuery<UserProfile> query = em.createQuery(OPENID_QUERY, UserProfile.class);
			query.setParameter("oid", id);
			List<UserProfile> profiles = query.getResultList();

			if (profiles.size() == 0) {
				em.getTransaction().begin();
				UserProfile profile = new UserProfile();
				profile.setName("");
				Collection<OpenIdEntity> ids = new Vector<OpenIdEntity>();

				OpenIdEntity openid = new OpenIdEntity();
				openid.setOpenId(id);
				openid.setEmail(OpenIdManager.instance().getEmail(request));
				ids.add(openid);

				profile.setOpenIds(ids);
				em.persist(profile);

				em.getTransaction().commit();
				return profile;
			} else {
				return profiles.get(0);
			}
		}

		return null;
	}
}
