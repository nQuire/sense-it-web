package org.greengin.nquireit.logic.rating;

import java.util.Collection;

import org.greengin.nquireit.entities.rating.Vote;
import org.greengin.nquireit.entities.users.UserProfile;

public class VoteCount {
	
	int positive;
	int negative;
	int total;
	
	Vote myVote;
	
	
	public VoteCount(Collection<Vote> votes, UserProfile selectedVoteAuthor) {
		positive = 0;
		negative = 0;
		myVote = null;
		
		for (Vote vote : votes) {
			if (vote.getValue() > 0) {
				positive++;
			} else if (vote.getValue() < 0) {
				negative++;
			}
			
			if (selectedVoteAuthor != null && selectedVoteAuthor.getId().equals(vote.getUser().getId())) {
				myVote = vote;
			}
		}

        total = positive + negative;
	}

	public int getPositive() {
		return positive;
	}

	public int getNegative() {
		return negative;
	}

	public int getTotal() {
		return total;
	}

	public Vote getMyVote() {
		return myVote;
	}
	
}
