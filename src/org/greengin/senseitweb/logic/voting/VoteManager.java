package org.greengin.senseitweb.logic.voting;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.entities.voting.Vote;

public class VoteManager {
	private static final String VOTE_QUERY = String.format(
			"SELECT v FROM %s v WHERE v.target = :target AND v.user = :user", Vote.class.getName());

	EntityManager em;
	UserProfile user;
	VotableEntity target;

	public VoteManager(EntityManager em, UserProfile user, VotableEntity target) {
		this.user = user;
		this.em = em;
		this.target = target;
	}

	public VoteCount vote(VoteRequest voteData) {

		TypedQuery<Vote> query = em.createQuery(VOTE_QUERY, Vote.class);
		query.setParameter("target", target);
		query.setParameter("user", user);

		List<Vote> votes = query.getResultList();

		Vote vote;

		em.getTransaction().begin();
		if (votes.size() == 0) {
			vote = new Vote();
			vote.setTarget(target);
			vote.setUser(user);
			voteData.update(vote);
			em.persist(vote);
			target.getVotes().add(vote);
		} else {
			vote = votes.get(0);
			voteData.update(vote);

			if (votes.size() > 1) {
				for (int i = 1; i < votes.size(); i++) {
					target.getVotes().remove(votes.get(i));
				}
			}
		}

		em.getTransaction().commit();

		target.selectVoteAuthor(user);
		return target.getVoteCount();
	}
}
