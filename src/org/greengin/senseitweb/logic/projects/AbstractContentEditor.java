package org.greengin.senseitweb.logic.projects;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.permissions.UsersManager;
import org.greengin.senseitweb.persistence.EMF;

public class AbstractContentEditor {
	protected UserProfile user;
	protected EntityManager em;
	protected boolean hasAccess;
	
	public AbstractContentEditor(HttpServletRequest request) {
		this.em = EMF.get().createEntityManager();
		this.user = UsersManager.get().currentUser(request);
		this.hasAccess = false;
	}

}
