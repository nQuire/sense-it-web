package org.greengin.nquireit.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class VoteDao {
    private static final String VOTE_QUERY = "SELECT v FROM Vote v WHERE v.target = :target AND v.user = :user";

    @PersistenceContext
    EntityManager em;

    @Transactional
    public VoteCount vote(UserProfile user, VotableEntity target, VoteRequest voteData) {

        TypedQuery<Vote> query = em.createQuery(VOTE_QUERY, Vote.class);
        query.setParameter("target", target);
        query.setParameter("user", user);

        List<Vote> votes = query.getResultList();

        Vote vote;

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

        return target.getVoteCount();
    }
}
