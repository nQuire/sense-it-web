package org.greengin.senseitweb.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("sense-it-web");

	private EMF() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}
}