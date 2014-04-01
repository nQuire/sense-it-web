package org.greengin.senseitweb.logic.moderation;

import javax.persistence.EntityManager;

import org.greengin.senseitweb.entities.voting.VotableEntity;

public class ModerationManager {
	EntityManager em;
	VotableEntity target;

	public ModerationManager(EntityManager em, VotableEntity target) {
		this.em = em;
		this.target = target;
	}

	public VotableEntity moderate(ModerationStatus status) {
		
		em.getTransaction().begin();
		target.setModerationStatus(status);
		em.getTransaction().commit();
		
		return target;
	}
}
