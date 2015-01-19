package org.greengin.nquireit.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.greengin.nquireit.entities.rating.VotableEntity;
import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.json.Views;
import org.greengin.nquireit.logic.log.LogManagerBean;
import org.greengin.nquireit.logic.rating.VoteCount;
import org.greengin.nquireit.logic.rating.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class VoteDao {
    private static final String VOTE_TARGET_USER_QUERY = "SELECT v FROM Vote v WHERE v.target = :target AND v.user = :user";
    private static final String VOTE_USER_QUERY = "SELECT v FROM Vote v WHERE v.user = :user";

    @PersistenceContext
    EntityManager em;

    @Autowired
    LogManagerBean logManager;

    @Transactional
    public Vote find(UserProfile user, VotableEntity target) {
        TypedQuery<Vote> query = em.createQuery(VOTE_TARGET_USER_QUERY, Vote.class);
        query.setParameter("target", target);
        query.setParameter("user", user);

        List<Vote> votes = query.getResultList();


        if (votes.size() == 0) {
            return null;
        } else {
            Vote vote = votes.get(0);
            if (votes.size() > 1) {
                for (int i = 1; i < votes.size(); i++) {
                    target.getVotes().remove(votes.get(i));
                    em.persist(target);
                    em.remove(votes.get(i));
                }
            }
            return vote;
        }
    }

    @Transactional
    public VoteCount vote(UserProfile user, VotableEntity target, VoteRequest voteData) {
        logManager.vote(user, target, voteData.getValue());

        Vote vote = find(user, target);

        if (vote == null) {
            vote = new Vote();
            vote.setTarget(target);
            target.getVotes().add(vote);
            vote.setUser(user);
        }

        voteData.update(vote);
        em.persist(vote);

        return target.getVoteCount();
    }

    @Transactional
    public void transferVotes(UserProfile fromUser, UserProfile toUser) {
        TypedQuery<Vote> query = em.createQuery(VOTE_USER_QUERY, Vote.class);
        query.setParameter("user", fromUser);

        for (Vote v : query.getResultList()) {
            Vote uv = find(toUser, v.getTarget());
            if (uv == null) {
                v.setUser(toUser);
                em.persist(v);
            } else {
                v.getTarget().getVotes().remove(v);
                em.persist(v.getTarget());
                em.remove(v);
            }
        }
    }
}
