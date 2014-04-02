package org.greengin.senseitweb.logic.voting;

import java.util.Collection;

import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.entities.rating.Vote;

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
			
			if (selectedVoteAuthor != null && selectedVoteAuthor.getId() == vote.getUser().getId()) {
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
