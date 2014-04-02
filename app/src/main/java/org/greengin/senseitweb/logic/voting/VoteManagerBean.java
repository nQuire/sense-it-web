package org.greengin.senseitweb.logic.voting;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.rating.VotableEntity;
import org.greengin.senseitweb.entities.rating.Vote;
import org.greengin.senseitweb.json.Views;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class VoteManagerBean {
	private static final String VOTE_QUERY = String.format(
			"SELECT v FROM %s v WHERE v.target = :target AND v.user = :user", Vote.class.getName());

    @Autowired
    CustomEntityManagerFactoryBean entityManagerFactory;


	public VoteCount vote(UserProfile user, VotableEntity target, VoteRequest voteData) {
        EntityManager em = entityManagerFactory.createEntityManager();

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

		target.setSelectedVoteAuthor(user);
		return target.getVoteCount();
	}
}
