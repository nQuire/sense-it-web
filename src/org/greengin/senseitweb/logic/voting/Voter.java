package org.greengin.senseitweb.logic.voting;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;

import org.greengin.senseitweb.entities.voting.VotableEntity;
import org.greengin.senseitweb.entities.voting.Vote;
import org.greengin.senseitweb.logic.AbstractContentEditor;

public class Voter<T extends VotableEntity> extends AbstractContentEditor {
	private static final String VOTE_QUERY = String.format(
			"SELECT v FROM %s v WHERE v.target = :target AND v.user = :user", Vote.class.getName());

	T target;
	Class<T> targetType;

	public Voter(HttpServletRequest request, Long targetId, Class<T> targetType) {
		super(request);
		if (hasAccess) {
			this.targetType = targetType;
			target = em.find(targetType, targetId);
			if (target == null) {
				hasAccess = false;
			}
		}
	}
	
	public VoteCount vote(VoteRequest voteData) {
		if (hasAccess) {
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
		} else {
			return null;
		}		
	}
}
